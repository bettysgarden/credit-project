package com.example.deal.exception;

import java.util.List;

public class ClientServiceException extends BaseCustomException {
    public static final String CLIENT_SERVICE_ERROR = "CLIENT_SERVICE_ERROR";

    public ClientServiceException(String message) {
        super(CLIENT_SERVICE_ERROR, message);
    }

    public ClientServiceException(String message, Throwable cause) {
        super(CLIENT_SERVICE_ERROR, message, List.of(cause.getMessage()));
    }
}

