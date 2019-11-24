package com.rubyhub.http.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.rubyhub.http.exceptions.HttpBadRequestException;
import com.rubyhub.http.responses.AppResponse;
import com.rubyhub.http.responses.ServiceResponse;
import com.rubyhub.http.utils.PATCH;
import com.rubyhub.managers.ArtworkManager;
import com.rubyhub.managers.ShoppingCartManager;
import com.rubyhub.managers.StudentManager;
import com.rubyhub.models.Artwork;
import com.rubyhub.models.CartItem;
import com.rubyhub.models.ShoppingCart;
import com.rubyhub.models.Student;
import org.codehaus.jettison.json.JSONArray;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Path("/carts")
public class ShoppingCartHttpInterface extends HttpInterface{
    private ObjectWriter ow;
    public ShoppingCartHttpInterface() {
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response shoppingcartRoot() {
        try {
            return ServiceResponse.response200("Hello, this is root of shopping cart");
        } catch (Exception e) {
            return Response.status(404).entity("Service is not available now, please try later").build();
        }
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse shoppingCartPost(Object request) {
        try{
            JSONObject json = null;
            System.out.println(request);
            json = new JSONObject(ow.writeValueAsString(request));
            String userId = json.getString("_id");
            System.out.println(userId);
            ShoppingCartManager.getInstance().createShoppingCart(userId);
            return new AppResponse("Insert Successful");

        }catch (Exception e){
            System.out.println(e.getMessage());
            throw handleException("POST students", e);
        }
    }

    @GET
    @Path("/all")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse shoppingCartGetAll() {
        try {
            List<ShoppingCart> shoppingCarts = ShoppingCartManager.getInstance().getCarts();
            if(shoppingCarts != null)
                return new AppResponse(shoppingCarts);
            else
                return new AppResponse("There is no shopping carts exist");
        } catch (Exception e) {
            throw new HttpBadRequestException(0, "Problem with getting shoppingCarts");
        }
    }

    @GET
    @Path("/{cartId}/artworks")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse shoppingCartGetOne( @PathParam("cartId") String cartId) {
        try {
            List<CartItem> artworks = ShoppingCartManager.getInstance().getCartById(cartId);
            return new AppResponse(artworks);
        } catch (Exception e) {
            throw new HttpBadRequestException(0, "Problem with getting shoppingCarts");
        }
    }

    @POST
    @Path("/{cartId}/artworks/{artId}")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public AppResponse addArtworkToShoppingCart(Object request, @PathParam("cartId") String cartId, @PathParam("artId") String artId){
        JSONObject json = null;

        try{
            json = new JSONObject(ow.writeValueAsString(request));
            String size = json.getString("size");

            ShoppingCartManager.getInstance().addArtwork(cartId, artId, size);

        }catch (Exception e){
            throw handleException("PATCH students/{studentId}", e);
        }

        return new AppResponse("Add Artwork Successful");
    }

    @DELETE
    @Path("/{cartId}/artworks")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public AppResponse deleteStudents(@PathParam("cartId") String cartId){

        try{
            ShoppingCartManager.getInstance().deleteAllArtworks(cartId);
            return new AppResponse("Delete Successful");
        }catch (Exception e){
            throw handleException("DELETE cartId/artworks", e);
        }

    }

    @DELETE
    @Path("/{cartId}/artworks/{artId}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public AppResponse deleteStudents(@PathParam("cartId") String cartId, @PathParam("artId") String artId){

        try{
            ShoppingCartManager.getInstance().deleteArtwork(cartId, artId);
            return new AppResponse("Delete Successful");
        }catch (Exception e){
            throw handleException("DELETE cartId/artworks", e);
        }

    }

    @POST
    @Path("/{cartId}/checkout")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public AppResponse checkout(@PathParam("cartId") String cartId){

        try{
            if(ShoppingCartManager.getInstance().checkout(cartId)) {
                ShoppingCartManager.getInstance().deleteAllArtworks(cartId);
                return new AppResponse("Checkout Successful");
            }
            else
                return new AppResponse("Failed to checkout");
        }catch (Exception e){
            throw handleException("Checkout cartId/artworks", e);
        }
    }
}
