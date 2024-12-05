package com.example.deal.exception;

import java.util.List;

public class StateTransitionException extends BaseCustomException {
    public static final String STATE_TRANSITION_ERROR = "STATE_ERROR";

    public StateTransitionException(String message, List<String> errors) {
        super(STATE_TRANSITION_ERROR, message, errors);
    }

}