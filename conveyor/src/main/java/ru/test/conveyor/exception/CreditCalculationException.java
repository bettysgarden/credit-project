package ru.test.conveyor.exception;

public class CreditCalculationException extends RuntimeException {

    public CreditCalculationException(String message, Throwable cause) {
        super(message, cause);
    }
}