package ru.test.conveyor.exception;

public class CreditCalculationException extends RuntimeException {
    public static final String CREDIT_CALC_ERROR = "CREDIT_CALC_ERROR";

    public CreditCalculationException(String message, Throwable cause) {
        super(message, cause);
    }
}