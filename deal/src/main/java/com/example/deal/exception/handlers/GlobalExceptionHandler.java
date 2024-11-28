package com.example.deal.exception.handlers;

import com.example.deal.exception.*;
import com.example.deal.model.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseCustomException.class)
    public ResponseEntity<ErrorResponse> handleBaseCustomException(BaseCustomException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getErrorCode(),
                ex.getMessage(),
                ex.getErrors()
        );
        HttpStatus status = determineHttpStatus(ex);
        return new ResponseEntity<>(errorResponse, status);
    }

    private HttpStatus determineHttpStatus(BaseCustomException ex) {
        if (ex instanceof InvalidLoanApplicationException || ex instanceof InvalidScoringDataException) {
            return HttpStatus.BAD_REQUEST;
        } else if (ex instanceof LoanCalculationException || ex instanceof CreditCalculationException) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        } else if (ex instanceof ConveyorFeignClientException) {
            return HttpStatus.SERVICE_UNAVAILABLE;
        } else if (ex instanceof DatabaseOperationException) {
            return HttpStatus.NOT_FOUND;
        } else if (ex instanceof StateTransitionException) {
            return HttpStatus.CONFLICT;
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}