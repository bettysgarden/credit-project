package com.example.deal.exception;

public class LoanCalculationException extends BaseCustomException {
    public static final String LOAN_CALCULATION_ERROR = "LOAN_CALCULATION_ERROR";

    public LoanCalculationException(String message) {
        super(LOAN_CALCULATION_ERROR, message);
    }
}
