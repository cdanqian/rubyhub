package com.rubyhub.http.responses;

import org.codehaus.jettison.json.JSONObject;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ServiceResponse {
    public static Response response200(Object content) {
        try {
            return Response.ok(new JSONObject().put("data", content).put("success", true).put("httpStatusCode", 200), MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(400).entity("Error while response encoding").build();
        }
    }
    public static Response response400(Object content) {
        try {
            return Response.status(400).entity(new JSONObject().put("data", content).put("success", false).put("httpStatusCode", 400)).build();
        } catch (Exception e) {
            return Response.status(400).entity("Error while response encoding").build();
        }
    }
    public static Response response409ConflictResource(Object content) {
        try {
            return Response.status(Response.Status.CONFLICT).entity(new JSONObject().put("data", content)).build();
        } catch (Exception e) {
            return Response.status(400).entity("Error while response encoding").build();
        }
    }
}
