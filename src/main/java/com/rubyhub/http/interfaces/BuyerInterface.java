package com.rubyhub.http.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.rubyhub.http.responses.ServiceResponse;
import com.rubyhub.http.utils.PATCH;
import com.rubyhub.managers.BuyerManager;
import com.rubyhub.models.Buyer;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Path("/buyers")
public class BuyerInterface extends HttpInterface {
    private ObjectWriter ow;

    public BuyerInterface() {
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response buyerRoot() {
        try {
            return ServiceResponse.response200("Hello, this is root of buyers");
        } catch (Exception e) {
            return Response.status(404).entity("Service is not available now, please try later").build();
        }
    }

    @POST
    @Path("")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response buyerPost(Object request) {
        try {
            JSONObject json = new JSONObject(ow.writeValueAsString(request));
            String email = json.getString("email");
            Buyer buyer = BuyerManager.getInstance().getByEmail(email);
            if (buyer == null) {
                BuyerManager.getInstance().create(
                        json.getString("first_name"),
                        json.getString("last_name"),
                        json.getString("email"),
                        json.getString("password")
                );
                buyer = BuyerManager.getInstance().getByEmail(email);
                return ServiceResponse.response200(buyer.castToJSON());
            } else {
                return ServiceResponse.response409ConflictResource("Buyer already exists");
            }
        } catch (Exception e) {
            throw handleException("POST /buyers", e);
        }
    }

    @GET
    @Path("/reset")
    @Produces({MediaType.APPLICATION_JSON})
    public Response resetResource() {
        BuyerManager.getInstance().resetDB();
        return ServiceResponse.response200("Buyer collection is ready");
    }

    @GET
    @Path("/all")
    @Produces({MediaType.APPLICATION_JSON})
    public Response allBuyersGet(@QueryParam("filter") String filter,
                                 @QueryParam("sortby") String sortby,
                                 @QueryParam("offset") Integer offset,
                                 @QueryParam("count") Integer count) {
        try {
            List<Buyer> buyers = null;
            if (sortby != null) {
                buyers = BuyerManager.getInstance().getAllBuyersSorted(sortby);
            } else if (filter != null) {
                buyers = BuyerManager.getInstance().getAllBuyersFiltered(filter);
            } else if (offset != null && count != null) {
                buyers = BuyerManager.getInstance().getAllBuyersPaginated(offset, count);
            } else {
                buyers = BuyerManager.getInstance().getAllBuyers();
            }
            List<JSONObject> content = buyers.stream().map(Buyer::castToJSON).collect(toList());
            return ServiceResponse.response200(new JSONArray(content));
        } catch (Exception e) {
            throw handleException("GET /buyers/id", e);
        }
    }

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response buyerGetById(@PathParam("id") String id) {
        try {
            JSONArray buyers = new JSONArray();
            Buyer buyer = BuyerManager.getInstance().getById(id);
            buyers.put(buyer.castToJSON());
            return ServiceResponse.response200(buyers);
        } catch (Exception e) {
            throw handleException("GET /buyers/id", e);
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response buyerDeleteById(@PathParam("id") String id) {
        try {
            BuyerManager.getInstance().delete(id);
            return ServiceResponse.response200("Buyer " + id + " has been deleted!");
        } catch (Exception e) {
            throw handleException("DELETE /buyers/id", e);
        }
    }

    @PATCH
    @Path("{id}/basic")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response buyerPatchById(Object request, @PathParam("id") String id) {
        try {
            JSONObject json = new JSONObject(ow.writeValueAsString(request));
            Buyer buyer = BuyerManager.getInstance().updateBasicInfo(
                    id,
                    json.getString("firstname"),
                    json.getString("lastname"),
                    json.getString("phone"),
                    json.getString("city"),
                    json.getString("country"),
                    json.getJSONObject("address")
            );
            JSONArray buyers = new JSONArray();
            buyers.put(buyer.castToJSON());
            return ServiceResponse.response200(buyers);
        } catch (Exception e) {
            throw handleException("PATCH /buyers/id/basic", e);
        }
    }
}
