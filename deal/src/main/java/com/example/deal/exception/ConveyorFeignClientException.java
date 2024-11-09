package com.example.deal.exception;

public class ConveyorFeignClientException extends RuntimeException {
    public ConveyorFeignClientException(String message, Throwable cause) {
        super(message, cause);
    }
}