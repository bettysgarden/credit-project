package ru.test.conveyor.exception;

import java.util.List;

public class InvalidScoringDataException extends RuntimeException {
    private final List<String> errors;

    public InvalidScoringDataException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
