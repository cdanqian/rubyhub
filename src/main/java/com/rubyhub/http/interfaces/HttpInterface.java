package com.rubyhub.http.interfaces;

import com.rubyhub.exceptions.AppException;
import com.rubyhub.http.exceptions.HttpBadRequestException;
import com.rubyhub.http.exceptions.HttpInternalServerException;
import com.rubyhub.utils.AppLogger;
import org.glassfish.jersey.server.ResourceConfig;
import org.json.JSONException;

import javax.ws.rs.WebApplicationException;

public class HttpInterface extends ResourceConfig {
    protected WebApplicationException handleException(String message, Exception e) {
        if (e instanceof JSONException)
            return new HttpBadRequestException(-1, "Bad request data provided: " + e.getMessage());
        if (e instanceof AppException)
            return ((AppException) e).getHttpException();
        AppLogger.error(message, e);
        return new HttpInternalServerException(-1);
    }
}
