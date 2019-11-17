package com.rubyhub.http.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.rubyhub.http.responses.ServiceResponse;
import com.rubyhub.managers.ArtworkManager;
import com.rubyhub.models.Artwork;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.List;

import static java.util.stream.Collectors.toList;

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

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response artworkGetById(@PathParam("id") String id
    ) {
        try {
            // todo: add file type check
            return ServiceResponse.response200(ArtworkManager.getInstance().getArtworkById(id).castToJSON());
        } catch (Exception e) {
            return Response.status(404).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/all")
    @Produces({MediaType.APPLICATION_JSON})
    public Response artworkGetAll(
    ) {
        try {
            // todo: add file type check
            List<Artwork> artworks = ArtworkManager.getInstance().getArtworks();
            List<JSONObject> content = artworks.stream().map(Artwork::castToJSON).collect(toList());
            return ServiceResponse.response200(new JSONArray(content));
        } catch (Exception e) {
            return Response.status(400).entity(e.getMessage()).build();
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
            return Response.status(400).entity("Service is not available now, please try later").build();
        }
    }

    @POST
    @Path("/image/{id}")
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    @Produces({MediaType.APPLICATION_JSON})
    public Response artworkImagePost(@PathParam("id") String id,
                                     @FormDataParam("image") InputStream image
    ) {
        try {
            // todo: add file type check
            ArtworkManager.getInstance().uploadArtworkImage(id, image);
            return ServiceResponse.response200(new JSONObject().put("id", "id"));
        } catch (Exception e) {
            return Response.status(400).entity(e.getMessage()).build();
        }
    }

}
