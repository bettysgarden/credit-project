package com.example.deal.exception;

import java.util.Collections;
import java.util.List;

public class ConveyorFeignClientException extends BaseCustomException {
    public static final String CONVEYOR_CLIENT_ERROR = "CONVEYOR_CLIENT_ERROR";

    public ConveyorFeignClientException(String message, Throwable cause) {
        super(CONVEYOR_CLIENT_ERROR, message,
                cause.getMessage() == null ? Collections.emptyList() : List.of(cause.getMessage()));
    }
}
