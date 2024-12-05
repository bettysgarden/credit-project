package ru.test.application.exception;


import java.util.Collections;
import java.util.List;

public class DealFeignClientException extends BaseCustomException {
    public static final String CONVEYOR_CLIENT_ERROR = "CONVEYOR_CLIENT_ERROR";

    public DealFeignClientException(String message, Throwable cause) {
        super(CONVEYOR_CLIENT_ERROR, message,
                cause.getMessage() == null ? Collections.emptyList() : List.of(cause.getMessage()));
    }
}
