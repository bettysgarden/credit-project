package com.example.deal.exception;

import java.util.List;

public class DatabaseOperationException extends BaseCustomException {
    public static final String DATABASE_OPERATION_ERROR = "REPOSITORY_ERROR";

    public DatabaseOperationException(String message, List<String> errors) {
        super(DATABASE_OPERATION_ERROR, message, errors);
    }
}