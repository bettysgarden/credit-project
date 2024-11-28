package com.example.deal.exception;

import java.util.List;

public class CreditCalculationException extends BaseCustomException {
    public static final String CREDIT_CALC_ERROR = "CREDIT_CALC_ERROR";

    public CreditCalculationException(String message, Throwable cause) {
        super(CREDIT_CALC_ERROR, message, List.of(cause.getMessage()));
    }
}
