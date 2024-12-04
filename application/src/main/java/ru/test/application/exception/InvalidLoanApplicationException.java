package ru.test.application.exception;

import java.util.List;

public class InvalidLoanApplicationException extends BaseCustomException {
    public static final String INVALID_LOAN_APPLICATION = "INVALID_LOAN_APPLICATION";

    public InvalidLoanApplicationException(String message, List<String> errors) {
        super(INVALID_LOAN_APPLICATION, message, errors);
    }
}
