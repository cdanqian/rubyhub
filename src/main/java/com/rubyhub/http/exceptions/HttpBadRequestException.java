package com.rubyhub.http.exceptions;

import com.rubyhub.exceptions.AppBadRequestException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class HttpBadRequestException extends WebApplicationException {
    public HttpBadRequestException(int errorCode, String errorMessage) {
        super(Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorModel(
                        Response.Status.BAD_REQUEST.getStatusCode(),
                        Response.Status.BAD_REQUEST.getReasonPhrase(),
                        errorCode,
                        errorMessage)
                ).type("application/json").build());
    }

    public HttpBadRequestException(int errorCode) {
        super(Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorModel(
                        Response.Status.BAD_REQUEST.getStatusCode(),
                        Response.Status.BAD_REQUEST.getReasonPhrase(),
                        errorCode)
                ).type("application/json").build());
    }

    public HttpBadRequestException(AppBadRequestException e) {
        super(Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorModel(
                        Response.Status.BAD_REQUEST.getStatusCode(),
                        Response.Status.BAD_REQUEST.getReasonPhrase(),
                        e.getErrorCode(),
                        e.getErrorMessage())
                ).type("application/json").build());
    }
}
