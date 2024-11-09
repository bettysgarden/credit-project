package com.example.deal.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class InvalidScoringDataException extends RuntimeException {
    public static final String INVALID_SCORING_DATA = "INVALID_SCORING_DATA";
    private final List<String> errors;

    public InvalidScoringDataException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }

}
