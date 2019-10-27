package com.rubyhub.http.exceptions;

import com.rubyhub.exceptions.AppUnauthorizedException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class HttpUnauthorizedException extends WebApplicationException {
    public HttpUnauthorizedException(AppUnauthorizedException e) {
        super(Response.status(Response.Status.UNAUTHORIZED).entity(new ErrorModel(
                Response.Status.UNAUTHORIZED.getStatusCode(),
                Response.Status.UNAUTHORIZED.getReasonPhrase(),
                e.getErrorCode(),
                e.getMessage())
        ).type("application/json").build());
    }
}
