package com.rubyhub.exceptions;

public class AppBadRequestException extends AppException {
    public static int ERROR_CODE_UNKNOWN_BOOK = 101;
    public static int ERROR_CODE_UNKNOWN_BORROWER = 102;
    public static int ERROR_CODE_UNSUPPORTED_PARAMETER = 103;
    public static int ERROR_CODE_DUPLICATE_ITEM = 104;
    public AppBadRequestException(int errorCode, String errorMessage) {
        super(AppException.BAD_REQUEST_EXCEPTION, errorCode, errorMessage);
    }

    public AppBadRequestException(int errorCode) {
        super(AppException.BAD_REQUEST_EXCEPTION, errorCode);
    }
}
