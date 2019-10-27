package com.rubyhub.http.interfaces;

import org.codehaus.jettison.json.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;

@Path("")
public class RubyhubInterfaces {
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response applicationRoot() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("version", "0.0.1");
            obj.put("date", String.join(" ", LocalDateTime.now().toString().split("T")));
            obj.put("info", "Hello from Rubyhub : ) ");
            return com.sem.fridgely.http.ServiceResponse.response200(obj);
        } catch (Exception e) {
            return Response.status(404).entity("Service is not available now, please try later").build();
        }
    }
}
