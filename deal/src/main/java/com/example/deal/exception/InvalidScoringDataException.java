package com.example.deal.exception;

import java.util.List;

public class InvalidScoringDataException extends BaseCustomException {
    public static final String INVALID_SCORING_DATA = "INVALID_SCORING_DATA";

    public InvalidScoringDataException(String message, List<String> errors) {
        super(INVALID_SCORING_DATA, message, errors);
    }
}
