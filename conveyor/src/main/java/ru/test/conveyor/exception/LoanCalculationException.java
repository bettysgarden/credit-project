package ru.test.conveyor.exception;

public class LoanCalculationException extends RuntimeException {
    public LoanCalculationException(String message) {
        super(message);
    }
}