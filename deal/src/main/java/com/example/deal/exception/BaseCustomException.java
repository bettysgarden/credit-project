package com.example.deal.exception;

import lombok.Getter;

import java.util.List;

@Getter
public abstract class BaseCustomException extends RuntimeException {
    private final String errorCode;
    private final List<String> errors;

    public BaseCustomException(String errorCode, String message, List<String> errors) {
        super(message);
        this.errorCode = errorCode;
        this.errors = errors != null ? errors : List.of();
    }

    public BaseCustomException(String errorCode, String message) {
        this(errorCode, message, null);
    }
}

