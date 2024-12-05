package ru.test.application.exception.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.test.application.exception.BaseCustomException;
import ru.test.application.model.dto.ErrorResponse;
import ru.test.application.exception.*;

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
        if (ex instanceof InvalidLoanApplicationException) {
            return HttpStatus.BAD_REQUEST;
        } else if (ex instanceof DealFeignClientException) {
            return HttpStatus.SERVICE_UNAVAILABLE;
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}