package com.rubyhub.managers;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.rubyhub.exceptions.AppException;
import com.rubyhub.exceptions.AppInternalServerException;
import com.rubyhub.models.Artwork;
import com.rubyhub.models.CartItem;
import com.rubyhub.models.Pricing;
import com.rubyhub.models.ShoppingCart;
import com.rubyhub.utils.MongoPool;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class ShoppingCartManager extends Manager {
    public static ShoppingCartManager _self;
    public MongoCollection<Document> shoppingCartCollection;
    public static String FIELD_ID = "_id", FIELD_ARTWORKS_LIST = "artworks";
    public static Map<String, String> sizeMaps = new HashMap<>();

    public ShoppingCartManager() {
        this.shoppingCartCollection = MongoPool.getInstance().getCollection("shoppingCarts");
    }

    public static ShoppingCartManager getInstance() {
        if (_self == null) {
            return new ShoppingCartManager();
        }
        return _self;
    }

    public void createShoppingCart(String userId) throws AppException {

        try{

            Document newDoc = new Document()
                    .append(FIELD_ID, userId)
                    .append(FIELD_ARTWORKS_LIST, new ArrayList<String>());
            if (newDoc != null)
                this.shoppingCartCollection.insertOne(newDoc);
            else
                throw new AppInternalServerException(0, "Failed to create new shopping cart");

        }catch(Exception e){
            System.out.println(e.getMessage());
            throw handleException("Create Shopping Cart", e);
        }

    }

    public ArrayList<ShoppingCart> getCarts() throws AppException {
        try{
            ArrayList<ShoppingCart> carts = new ArrayList<>();
            FindIterable<Document> cartDocs = shoppingCartCollection.find();
            for(Document cartDoc: cartDocs) {
                ShoppingCart cart = new ShoppingCart(
                        cartDoc.getString(FIELD_ID),
                        cartDoc.get(FIELD_ARTWORKS_LIST, ArrayList.class)
                );
                carts.add(cart);
            }
            return new ArrayList<>(carts);
        } catch(Exception e){
            throw handleException("Get Cart List", e);
        }
    }

    public ArrayList<CartItem> getCartById(String cartId) throws AppException {
        try{
            ArrayList<CartItem> items = new ArrayList<>();
            FindIterable<Document> cartDocs = shoppingCartCollection.find();
            for(Document cartDoc: cartDocs) {
                if(cartDoc.getString(FIELD_ID).equals(cartId)) {
                    ArrayList<String> artworkIds = cartDoc.get(FIELD_ARTWORKS_LIST, ArrayList.class);
                    for(String artworkId : artworkIds){
                        Artwork artwork = ArtworkManager.getInstance().getArtworkById(artworkId);
                        CartItem item = new CartItem(
                                artworkId,
                                artwork.getName(),
                                artwork.getPrice(),
                                artwork.getImage(),
                                sizeMaps.get(cartId + artworkId)
                                );
                        items.add(item);
                    }
                }
                }
            return items;
            }
        catch(Exception e){
            throw handleException("Get Student ", e);
        }
    }

    public void addArtwork(String cartId, String artId, String size){
        try{
            Document doc = this.shoppingCartCollection.find(eq(FIELD_ID, cartId)).first();
            ShoppingCart cart = new ShoppingCart(doc.getString(FIELD_ID),
                    doc.get(FIELD_ARTWORKS_LIST,ArrayList.class));
            ArrayList<String> artworks = cart.getArtworks();
            sizeMaps.put(cartId + artId, size);
            artworks.add(artId);
            this.shoppingCartCollection.findOneAndUpdate(eq(FIELD_ID, cartId),
                    new Document("$set", new Document(FIELD_ARTWORKS_LIST, artworks)
                            ));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteAllArtworks(String cartId){
        this.shoppingCartCollection.findOneAndUpdate(eq(FIELD_ID, cartId),
                new Document("$set", new Document(FIELD_ARTWORKS_LIST, new ArrayList<String>())
                ));
        ArrayList<String> artworkIds = this.shoppingCartCollection.find(eq(FIELD_ID, cartId)).first().get(FIELD_ARTWORKS_LIST, ArrayList.class);
        for(String artId : artworkIds){
            sizeMaps.put(cartId + artId, null);
        }
    }

    public void deleteArtwork(String cartId, String artId){
        try{
            Document doc = this.shoppingCartCollection.find(eq(FIELD_ID, cartId)).first();
            ShoppingCart cart = new ShoppingCart(doc.getString(FIELD_ID),
                    doc.get(FIELD_ARTWORKS_LIST,ArrayList.class));
            ArrayList<String> artworks = cart.getArtworks();
            sizeMaps.put(cartId + artId, null);
            artworks.remove(artId);
            this.shoppingCartCollection.findOneAndUpdate(eq(FIELD_ID, cartId),
                    new Document("$set", new Document(FIELD_ARTWORKS_LIST, artworks)
                    ));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkout(String cartId) throws AppException {
        ArrayList<CartItem> items = this.getCartById(cartId);
        double price = 0.0;
        for(CartItem item : items){
             price += PricingManager.getInstance().getPrice(item.getSize()).getTotal();
        }
        if(PaymentManager.getInstance().doPayment(price))
            return true;
        else
            return false;
    }
}
