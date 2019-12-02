package com.rubyhub.managers;

import com.rubyhub.models.Pricing;
import org.bson.Document;

import java.util.HashMap;

import static com.mongodb.client.model.Filters.eq;

public class PricingManager extends Manager {
    private static final Double FACTOR_LIKES = 0.2, FACTOR_TAX = 0.01;
    public static PricingManager _self;
    private static HashMap<String, Double> FACTOR_SIZES = new HashMap();


    public static PricingManager getInstance() {
        if (_self == null) {
            setFactorSizes();
            return new PricingManager();
        }
        return _self;
    }

    public Pricing getPrice(String id, String size) {
        double originalPrice = 0, charge4Size = 0, bonusOfLikes = 0, tax = 0;
        Document doc = this.artworkCollection.find(eq(ArtworkManager.FIELD_ID, id)).first();
        if (doc != null) {
            originalPrice = doc.getDouble(ArtworkManager.FIELD_PRICE);
            bonusOfLikes = doc.getInteger(ArtworkManager.FIELD_LIKES) * FACTOR_LIKES;
            charge4Size = originalPrice * FACTOR_SIZES.get(size.toUpperCase()).doubleValue();
            tax = originalPrice * FACTOR_TAX;
        }
        return new Pricing(originalPrice, charge4Size, bonusOfLikes, tax);
    }
    private static void setFactorSizes(){
        FACTOR_SIZES.put("S", 0.1);
        FACTOR_SIZES.put("M", 0.2);
        FACTOR_SIZES.put("L", 0.3);
    }
}
