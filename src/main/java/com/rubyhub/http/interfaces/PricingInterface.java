package com.rubyhub.http.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.rubyhub.http.responses.ServiceResponse;
import com.rubyhub.managers.PricingManager;
import com.rubyhub.models.Pricing;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/pricing")
public class PricingInterface extends HttpInterface{
    private ObjectWriter ow;
    public PricingInterface(){
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response pricingRoot() {
        try {
            return ServiceResponse.response200("Hello, this is root of pricing");
        } catch (Exception e) {
            return Response.status(404).entity("Service is not available now, please try later").build();
        }
    }

   @POST
   @Path("/{id}")
   @Produces({MediaType.APPLICATION_JSON})
    public Response pricingGet(Object request, @PathParam("id") String id){
       try {
           JSONObject json = new JSONObject(ow.writeValueAsString(request));
           String size = json.getString("size");
           Pricing price = PricingManager.getInstance().getPrice(id,size );
           return ServiceResponse.response200(price.castToJSON());
       } catch (JSONException e) {
           e.printStackTrace();
       } catch (JsonProcessingException e) {
           e.printStackTrace();
       }
       return ServiceResponse.response200(133.3);
   }
}
