package com.rubyhub.managers;


import com.rubyhub.utils.PayPalClient;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class PaymentManager extends Manager {
    public static PaymentManager _self;

    public static PaymentManager getInstance() {
        if (_self == null) {
            return new PaymentManager();
        }
        return _self;
    }

    public Boolean doPayment(double price) {
        // todo: include payment service
        return price != 0;
    }

//    public Boolean doPayment(double amount) {
//
//        String approvedOrderId = null;
//        try {
//            approvedOrderId = PayPalClient.createOrder(amount).getString("id");
//        } catch (JSONException e) {
//            return false;
//        }
//        String captureId = PayPalClient.captureOrder(approvedOrderId);
//        return PayPalClient.getPaymentResult(captureId);
//
//    }

    public JSONObject createPayment(Double amount) {
        return PayPalClient.createOrder(amount);
    }

    public void capturePayment(String approvedOrderId) {
        PayPalClient.captureOrder(approvedOrderId);

    }
}
