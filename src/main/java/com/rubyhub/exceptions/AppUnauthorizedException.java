package com.rubyhub.exceptions;

public class AppUnauthorizedException extends AppException {
    public AppUnauthorizedException(int errorCode, String errorMessage) {
        super(AppException.UNAUTHORIZED_EXCEPTION, errorCode, errorMessage);
    }
    public AppUnauthorizedException(int errorCode) {
        super(AppException.UNAUTHORIZED_EXCEPTION, errorCode);
    }
}
