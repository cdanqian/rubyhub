package com.rubyhub.models;

import org.codehaus.jettison.json.JSONObject;

public class Pricing {

    private Double total, originalPrice, charge4Size, bonusOfLikes, tax;

    public Pricing(Double originalPrice, Double charge4Size, Double bonusOfLikes, Double tax) {
        this.bonusOfLikes = bonusOfLikes;
        this.charge4Size = charge4Size;
        this.originalPrice = originalPrice;
        this.tax = tax;
        this.total = bonusOfLikes + charge4Size + originalPrice + tax;
    }

    public Double getTotal() {
        return total;
    }

    public JSONObject castToJSON() {
        try {
            JSONObject js = new JSONObject()
                    .put("total", total)
                    .put("detail", new JSONObject().put("originalPrice", originalPrice)
                            .put("charge4size", charge4Size)
                            .put("bonusOfLikes", bonusOfLikes)
                            .put("tax", tax));
            return js;
        } catch (Exception e) {
            return null;
        }
    }
}
