package ru.test.conveyor.exception;

public class LoanCalculationException extends RuntimeException {
    public static final String LOAN_CALCULATION_ERROR = "LOAN_CALCULATION_ERROR";

    public LoanCalculationException(String message) {
        super(message);
    }
}