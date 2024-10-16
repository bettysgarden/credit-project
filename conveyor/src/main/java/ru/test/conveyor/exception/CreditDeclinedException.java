package ru.test.conveyor.exception;

public class CreditDeclinedException extends RuntimeException {
    public CreditDeclinedException(String message) {
        super(message);
    }
}
