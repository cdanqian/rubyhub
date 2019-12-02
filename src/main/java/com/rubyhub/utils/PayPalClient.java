package com.rubyhub.utils;


import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.http.exceptions.HttpException;
import com.paypal.http.serializer.Json;
import com.paypal.orders.*;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PayPalClient {

    /**
     * Set up the PayPal Java SDK environment with PayPal access credentials.
     * This sample uses SandboxEnvironment. In production, use LiveEnvironment.
     */
    private static PayPalEnvironment environment = new PayPalEnvironment.Sandbox(
            "Aa6t7hEglwiTcgxMYmNRNJA9dINyOCxSt8f3xW9PXuE1gDCMLsPQOqa379yACiQy7qm1muGyubkqZCpR",
            "EF6wGfCDGPynqNHn2q7O3DzSqYY9NB7QIwkQFLRlhuOKLDDFT1zd-fwrkTPrFgB4JfmLUt2ckBENglAQ");

    /**
     * PayPal HTTP client instance with environment that has access
     * credentials context. Use to invoke PayPal APIs.
     */
    public static PayPalHttpClient client = new PayPalHttpClient(environment);

    /**
     * Method to get client object
     *
     * @return PayPalHttpClient client
     */

    public static void getOrder(String orderId) throws IOException, JSONException {
        OrdersGetRequest request = new OrdersGetRequest(orderId);
        //3. Call PayPal to get the transaction
        HttpResponse<Order> response = client.execute(request);
        //4. Save the transaction in your database. Implement logic to save transaction to your database for future reference.
        System.out.println("Full response body:");
        System.out.println(new JSONObject(new Json().serialize(response.result())).toString(4));
    }

    public static JSONObject createOrder(Double amount) {
        Order order = null;
        // Construct a request object and set desired parameters
        // Here, OrdersCreateRequest() creates a POST request to /v2/checkout/orders
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.checkoutPaymentIntent("CAPTURE");
        List<PurchaseUnitRequest> purchaseUnits = new ArrayList<>();
        purchaseUnits
                .add(new PurchaseUnitRequest().amountWithBreakdown(new AmountWithBreakdown().currencyCode("USD").value(amount.toString())));
        orderRequest.purchaseUnits(purchaseUnits);
        OrdersCreateRequest request = new OrdersCreateRequest().requestBody(orderRequest);

        try {
            JSONObject result = new JSONObject();
            // Call API with your client and get a response for your call
            HttpResponse<Order> response = client.execute(request);

            // If call returns body in response, you can get the de-serialized version by
            // calling result() on the response

            order = response.result();
            try {
                result.put("id", order.id());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //            order.links().forEach(link -> System.out.println(link.rel() + " => " + link.method() + ":" + link.href()));
            order.links().forEach(link ->
            {
                try {
                    result.put(link.rel(), link.href());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });

            return result;
        } catch (IOException ioe) {
            if (ioe instanceof HttpException) {
                // Something went wrong server-side
                HttpException he = (HttpException) ioe;
                System.out.println(he.getMessage());
                he.headers().forEach(x -> System.out.println(x + " :" + he.headers().header(x)));
            } else {
                // Something went wrong client-side
            }
        }
        return null;
    }

    public static String captureOrder(String approvedOrderId) {
        Order order = null;
        OrdersCaptureRequest request = new OrdersCaptureRequest(approvedOrderId);
        String captureId = null;
        try {
            // Call API with your client and get a response for your call
            HttpResponse<Order> response = client.execute(request);
            // If call returns body in response, you can get the de-serialized version by
            // calling result() on the response
            order = response.result();
            System.out.println("Capture ID: " + order.purchaseUnits().get(0).payments().captures().get(0).id());
            order.purchaseUnits().get(0).payments().captures().get(0).links()
                    .forEach(link -> System.out.println(link.rel() + " => " + link.method() + ":" + link.href()));
            return order.purchaseUnits().get(0).payments().captures().get(0).id();
        } catch (IOException ioe) {
            if (ioe instanceof HttpException) {
                // Something went wrong server-side
                HttpException he = (HttpException) ioe;
                System.out.println(he.getMessage());
                he.headers().forEach(x -> System.out.println(x + " :" + he.headers().header(x)));
            } else {
                AppLogger.error("Capture order exception", ioe);
            }
        }
        return captureId;
    }

    public static boolean getPaymentResult(String captureId) {
        // this payment result is supposed to be show in UI, as there is no UI available for now,
        // always return payment successful
        return true;
    }
}