package com.rubyhub.http.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.rubyhub.http.responses.ServiceResponse;
import com.rubyhub.http.utils.PATCH;
import com.rubyhub.managers.ArtworkManager;
import com.rubyhub.models.Artwork;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import java.io.*;
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
    @Path("/reset")
    @Produces({MediaType.APPLICATION_JSON})
    public Response artworkRootReset() {
        try {
            ArtworkManager.getInstance().resetData();
            return ServiceResponse.response200("Artwork resource has been reset");
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
            Artwork artwork = ArtworkManager.getInstance().getArtworkById(id);
            if (artwork == null) return ServiceResponse.response200(new JSONObject());
            return ServiceResponse.response200(artwork.castToJSON());
        } catch (Exception e) {
            return Response.status(404).entity(e.getMessage()).build();
        }
    }

    @PATCH
    @Path("/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response artworkPatchById(Object request, @PathParam("id") String id) {
        try {
            JSONObject json = new JSONObject(ow.writeValueAsString(request));
            Boolean updated = ArtworkManager.getInstance().updateArtwork(
                    id,
                    json.getString("name"),
                    json.getString("description"),
                    json.getJSONArray("styles")
            );
            if (!updated) {
                return ServiceResponse.response400("ID is not valid");
            }
            return ServiceResponse.response200("Updated");
        } catch (Exception e) {
            throw handleException("PATCH /artwork/id", e);
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response artworkDELETE(@PathParam("id") String id) {
        try {
            ArtworkManager.getInstance().deleteArtwork(id);
            return ServiceResponse.response200("deleted");
        } catch (Exception e) {
            throw handleException("PATCH /artwork/id", e);
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
    public Response artworkImagePost(
            @PathParam("id") String id,
            @FormDataParam("image") InputStream image,
            @FormDataParam("image") FormDataContentDisposition fdc
                                     ) {
        try {
            //todo: get file type from FormDataContentDisposition
            System.out.println(fdc.getFileName());
            ArtworkManager.getInstance().uploadArtworkImage(id, image, "jpg");
            return ServiceResponse.response200("Uploaded");
        } catch (Exception e) {
            return Response.status(400).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/image/{filename}")
    @Produces("image/jpeg")
    public Response artworkImageGet(@PathParam("filename") String filename) {
        try {
            byte[] image = ArtworkManager.getInstance().getArtworkImageById(filename);
            return Response.ok(image).build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
