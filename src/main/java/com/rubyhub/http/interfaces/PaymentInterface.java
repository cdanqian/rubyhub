package com.rubyhub.http.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.rubyhub.http.responses.ServiceResponse;
import com.rubyhub.managers.PaymentManager;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/payment")
public class PaymentInterface extends HttpInterface {
    private ObjectWriter ow;

    public PaymentInterface() {
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    @GET
    @Path("")
    @Produces({MediaType.APPLICATION_JSON})
    public Response paymentRoot() {
        try {
            return ServiceResponse.response200("Hello, this is root of payment");
        } catch (Exception e) {
            return Response.status(404).entity("Service is not available now, please try later").build();
        }
    }

    @POST
    @Path("")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response paymentCreate(Object request) {
        try {
            JSONObject json = new JSONObject(ow.writeValueAsString(request));
            Double amount = json.getDouble("amount");
            JSONObject result = PaymentManager.getInstance().createPayment(amount);
            return ServiceResponse.response200(result);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return ServiceResponse.response200(133.3);
    }

    @POST
    @Path("/capture/{orderId}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response paymentCapture(@PathParam("orderId") String id) {
        PaymentManager.getInstance().capturePayment(id);
        return ServiceResponse.response200("captured");
    }

}
