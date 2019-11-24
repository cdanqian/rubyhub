package com.rubyhub.models;

public class Pricing {

    private Double total, originalPrice, charging4Size, bonusOfLikes, tax;

    public Pricing(Double originalPrice, Double charging4Size, Double bonusOfLikes, Double tax) {
        this.bonusOfLikes = bonusOfLikes;
        this.charging4Size = charging4Size;
        this.originalPrice = originalPrice;
        this.total = bonusOfLikes + charging4Size + originalPrice + tax;
    }

    public  Double getTotal(){
        return this.getTotal();
    }
}
