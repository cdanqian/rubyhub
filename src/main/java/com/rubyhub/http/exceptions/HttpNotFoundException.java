package com.rubyhub.http.exceptions;

import com.rubyhub.exceptions.AppNotFoundException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class HttpNotFoundException  extends WebApplicationException {
    public HttpNotFoundException(int errorCode, String errorMessage) {
        super(Response.status(Response.Status.NOT_FOUND).entity(new ErrorModel(
                Response.Status.NOT_FOUND.getStatusCode(),
                Response.Status.NOT_FOUND.getReasonPhrase(),
                errorCode,
                errorMessage)
        ).type("application/json").build());
    }

    public HttpNotFoundException(int errorCode) {
        super(Response.status(Response.Status.NOT_FOUND).entity(new ErrorModel(
                Response.Status.NOT_FOUND.getStatusCode(),
                Response.Status.NOT_FOUND.getReasonPhrase(),
                errorCode)
        ).type("application/json").build());
    }
    public HttpNotFoundException(AppNotFoundException e) {
        super(Response.status(Response.Status.NOT_FOUND).entity(new ErrorModel(
                Response.Status.NOT_FOUND.getStatusCode(),
                Response.Status.NOT_FOUND.getReasonPhrase(),
                e.getErrorCode(),
                e.getErrorMessage())
        ).type("application/json").build());
    }
}
