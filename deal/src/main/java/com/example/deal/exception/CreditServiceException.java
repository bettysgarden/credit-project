package com.example.deal.exception;

import java.util.List;

public class CreditServiceException extends BaseCustomException {
    public static final String CREDIT_SERVICE_ERROR = "CREDIT_SERVICE_ERROR";

    public CreditServiceException(String message, Throwable cause) {
        super(CREDIT_SERVICE_ERROR, message, List.of(cause.getMessage()));
    }
}


