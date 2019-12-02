package com.rubyhub.http.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.rubyhub.http.responses.ServiceResponse;
import com.rubyhub.http.utils.PATCH;
import com.rubyhub.managers.ArtworkManager;
import com.rubyhub.models.Artwork;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Path("/explores")
public class ExploreHttpInterface extends HttpInterface {
    private ObjectWriter ow;

    public ExploreHttpInterface() {
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response exploreRoot() {
        try {
            return ServiceResponse.response200("Hello, this is root of explores");
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
    @Path("/all")
    @Produces({MediaType.APPLICATION_JSON})
    public Response artworkGetAll(@QueryParam("q") String query) {
        List<Artwork> artworks = new ArrayList<Artwork>();
        System.out.println(query);
        try {
            if (query != null) {

                artworks = ArtworkManager.getInstance().searchArtworks(query);
            } else {
                artworks = ArtworkManager.getInstance().getArtworks();
            }

            List<JSONObject> content = artworks.stream().map(Artwork::castToJSON).collect(toList());
            return ServiceResponse.response200(new JSONArray(content));
        } catch (Exception e) {
            return Response.status(400).entity(e.getMessage()).build();
        }
    }

    @PATCH
    @Path("/{id}/likes")
    @Produces({MediaType.APPLICATION_JSON})
    public Response artworkLikesPatchById(@PathParam("id") String id) {
        try {
            Boolean updated = ArtworkManager.getInstance().markArtworkLikes(id);
            if (!updated) {
                return ServiceResponse.response400("ID is not valid");
            }
            return ServiceResponse.response200("Marks a like of an artwork");
        } catch (Exception e) {
            throw handleException("PATCH /artwork/id", e);
        }
    }

    @PATCH
    @Path("/{id}/unlikes")
    @Produces({MediaType.APPLICATION_JSON})
    public Response artworkCancelLikesById(@PathParam("id") String id) {
        try {
            Boolean updated = ArtworkManager.getInstance().unMarkArtworkLikes(id);
            if (!updated) {
                return ServiceResponse.response400("ID is not valid");
            }
            return ServiceResponse.response200("Cancelled one like of an artwork");
        } catch (Exception e) {
            throw handleException("PATCH /artwork/id", e);
        }
    }

    //Sorting: http://localhost:8080/api/explores/sort?sortby=likesAsc
    @GET
    @Path("/sortlikes")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getArtworksSortedByLikes(@QueryParam("sortby") String sortby) {
        try {
            ArrayList<Artwork> artworks = null;

            //Sorting
            if (sortby != null)
                artworks = ArtworkManager.getInstance().getArtworkListSortedByLikes(sortby);
            List<JSONObject> content = artworks.stream().map(Artwork::castToJSON).collect(toList());
            return ServiceResponse.response200(new JSONArray(content));

        } catch (Exception e) {
            throw handleException("GET /explores/all", e);
        }
    }

    //Sorting: http://localhost:8080/api/explores/sort?sortby=timeAsc
    @GET
    @Path("/sorttime")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getArtworksSortedByTime(@QueryParam("sortby") String sortby) {
        try {
            ArrayList<Artwork> artworks = null;

            //Sorting
            if (sortby != null)
                artworks = ArtworkManager.getInstance().getArtworkListSortedByTime(sortby);
            List<JSONObject> content = artworks.stream().map(Artwork::castToJSON).collect(toList());
            return ServiceResponse.response200(new JSONArray(content));

        } catch (Exception e) {
            throw handleException("GET /explores/all", e);
        }
    }

    @GET
    @Path("/filter")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getArtworksFilteredByStyles(@QueryParam("student") String filter) {
        try {
            ArrayList<Artwork> artworks = null;

            artworks = ArtworkManager.getInstance().getAllArtworksFilteredByOwners(filter);
            List<JSONObject> content = artworks.stream().map(Artwork::castToJSON).collect(toList());
            return ServiceResponse.response200(new JSONArray(content));

        } catch (Exception e) {
            throw handleException("GET /explores/all", e);
        }
    }

}
