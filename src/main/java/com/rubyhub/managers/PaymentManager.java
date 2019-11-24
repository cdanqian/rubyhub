package com.rubyhub.managers;


import com.rubyhub.models.Pricing;

public class PaymentManager extends Manager {
    public static PaymentManager _self;

    public static PaymentManager getInstance() {
        if (_self == null) {
            return new PaymentManager();
        }
        return _self;
    }

    public Boolean doPayment(Pricing price) {
        // todo: include payment service
        return price.getTotal() != 0;
    }
}