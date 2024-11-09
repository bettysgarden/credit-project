package com.example.deal.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class CreditDeclinedException extends RuntimeException {
    public static final String CREDIT_DECLINED_ERROR = "CREDIT_DECLINED_ERROR";
    private final List<String> errors;

    public CreditDeclinedException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }

}
