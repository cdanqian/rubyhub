package com.rubyhub.managers;

import com.rubyhub.models.Pricing;

public class PricingManager extends Manager {
    public static PricingManager _self;

    public static PricingManager getInstance() {
        if (_self == null) {
            return new PricingManager();
        }
        return _self;
    }

    public Pricing getPrice(String size) {
        double originalPrice = 10.1, charging4Size = 1.1, bonusOfLikes = 2.1, tax = 3.1;
        if (size.toUpperCase() == "S") {
            return new Pricing(originalPrice, charging4Size, bonusOfLikes, tax);
        }
        return new Pricing((double) 0, (double) 0, (double) 0, (double) 0);
    }
}
