package com.rubyhub.exceptions;

public class AppInternalServerException extends AppException {
    public static int ERROR_CODE_DB_ERROR = 201;
    public AppInternalServerException(int errorCode, String errorMessage) {
        super(AppException.INTERNAL_SERVER_EXCEPTION, errorCode, errorMessage);
    }
    public AppInternalServerException(int errorCode) {
        super(AppException.INTERNAL_SERVER_EXCEPTION, errorCode);
    }
}
