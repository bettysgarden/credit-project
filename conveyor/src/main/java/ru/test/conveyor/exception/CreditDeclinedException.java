package ru.test.conveyor.exception;

import java.util.List;

public class CreditDeclinedException extends RuntimeException {
    private final List<String> errors;

    public CreditDeclinedException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
