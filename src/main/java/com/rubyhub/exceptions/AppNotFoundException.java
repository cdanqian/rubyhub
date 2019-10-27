package com.rubyhub.exceptions;

public class AppNotFoundException extends AppException {
    public AppNotFoundException(int errorCode, String errorMessage) {
        super(AppException.NOT_FOUND_EXCEPTION, errorCode, errorMessage);
    }

    public AppNotFoundException(int errorCode) {
        super(AppException.NOT_FOUND_EXCEPTION, errorCode);
    }
}
