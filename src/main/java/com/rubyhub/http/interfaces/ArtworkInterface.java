package com.rubyhub.http.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.rubyhub.http.responses.ServiceResponse;
import com.rubyhub.managers.ArtworkManager;
import com.sun.jersey.core.header.FormDataContentDisposition;
import org.codehaus.jettison.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

@Path("/artworks")
public class ArtworkInterface extends HttpInterface {
    private ObjectWriter ow;

    public ArtworkInterface() {
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response artworkRoot() {
        try {
            return ServiceResponse.response200("Hello, this is root of artworks");
        } catch (Exception e) {
            return Response.status(404).entity("Service is not available now, please try later").build();
        }
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response artworkPost(Object request) {
        try {
            JSONObject json = new JSONObject(ow.writeValueAsString(request));
            String id = ArtworkManager.getInstance().createArtwork(
                    json.getString("name"),
                    json.getString("description"),
                    json.getDouble("price"),
                    json.getJSONArray("sizes"),
                    json.getJSONArray("styles"),
                    json.getString("student") // todo: read student from session
            );
            return ServiceResponse.response200(new JSONObject().put("id", id));
        } catch (Exception e) {
            return Response.status(404).entity("Service is not available now, please try later").build();
        }
    }

    @POST
    @Path("/{id}")
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    @Produces({MediaType.APPLICATION_JSON})
    public Response artworkImagePost(@PathParam("id") String id
//                                     @FormParam("File") FileInputStream image
//                                     @FormParam("image") FormDataContentDisposition cdh
    ) {
        try {
//            System.out.println(form.get("image"));
//            ArtworkManager.getInstance().uploadArtworkImage(id,  image);
            return ServiceResponse.response200(new JSONObject().append("id", id));
        } catch (Exception e) {
            return Response.status(404).entity("Service is not available now, please try later").build();
        }
    }
}
