package com.example.deal.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class InvalidLoanApplicationException extends RuntimeException {

    public static final String INVALID_LOAN_APPLICATION = "INVALID_LOAN_APPLICATION";
    private final List<String> errors;

    public InvalidLoanApplicationException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }

}
