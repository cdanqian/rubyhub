package com.rubyhub.exceptions;

import com.rubyhub.http.exceptions.HttpBadRequestException;
import com.rubyhub.http.exceptions.HttpInternalServerException;
import com.rubyhub.http.exceptions.HttpNotFoundException;
import com.rubyhub.http.exceptions.HttpUnauthorizedException;

import javax.ws.rs.WebApplicationException;

public class AppException extends Exception{
    public static final int INTERNAL_SERVER_EXCEPTION = 0;
    public static final int NOT_FOUND_EXCEPTION = 1;
    public static final int BAD_REQUEST_EXCEPTION = 2;
    public static final int UNAUTHORIZED_EXCEPTION = 3;

    int type;
    private int errorCode;
    private String errorMessage;

    public AppException(int type, int errorCode, String errorMessage){
        this.type = type;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
    public AppException(int type, int errorCode){
        this.type = type;
        this.errorCode = errorCode;
        this.errorMessage = null;
    }
    public int getType(){
        return this.type;
    }
    public int getErrorCode(){ return this.errorCode;}
    public String getErrorMessage(){ return this.errorMessage;}
    public WebApplicationException getHttpException(){
        switch(type) {
            case AppException.NOT_FOUND_EXCEPTION:
                return new HttpNotFoundException((AppNotFoundException)this);

            case AppException.BAD_REQUEST_EXCEPTION:
                return new HttpBadRequestException((AppBadRequestException)this);

            case AppException.UNAUTHORIZED_EXCEPTION:
                return new HttpUnauthorizedException((AppUnauthorizedException)this);

            default:
                return new HttpInternalServerException((AppInternalServerException)this);
        }
    }
}
