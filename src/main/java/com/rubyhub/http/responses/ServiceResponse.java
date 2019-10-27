package com.rubyhub.http.responses;

import org.codehaus.jettison.json.JSONObject;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ServiceResponse {
    public static Response response200(Object content) {
        JSONObject rs = new JSONObject();
        try {
            return Response.ok(new JSONObject().put("data", content), MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(400).entity("Error while response encoding").build();
        }
    }
}
