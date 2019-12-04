package com.rubyhub.http.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.rubyhub.http.responses.ServiceResponse;
import com.rubyhub.managers.ImageInspectionManager;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;

@Path("/inspection")
public class ImageInspectionInterface {
    private ObjectWriter ow;

    public ImageInspectionInterface() {
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response artworkRoot() {
        try {

            return ServiceResponse.response200("Hello, this is root of image inspection service");
        } catch (Exception e) {
            return Response.status(404).entity("Service is not available now, please try later").build();
        }
    }

    @POST
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    @Produces({MediaType.APPLICATION_JSON})
    public Response artworkImagePost(
            @FormDataParam("image") InputStream image,
            @FormDataParam("image") FormDataContentDisposition fdc
    ) {
        try {
            //todo: get file type from FormDataContentDisposition
            String fileType = fdc.getFileName().split("\\.")[1];
            ImageInspectionManager inspectionManager = ImageInspectionManager.getInstance().doInspection(image, fileType);

            return ServiceResponse.response200(new JSONObject()
                    .put("passed_check", inspectionManager.getPassed())
                    .put("messages", new JSONArray(inspectionManager.getMessages())));
        } catch (Exception e) {
            return Response.status(400).entity(e.getMessage()).build();
        }
    }
}
