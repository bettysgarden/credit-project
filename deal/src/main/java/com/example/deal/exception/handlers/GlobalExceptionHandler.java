package com.example.deal.exception.handlers;

import com.example.deal.exception.*;
import com.example.deal.model.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidLoanApplicationException.class)
    public ResponseEntity<ErrorResponse> handleInvalidLoanApplication(InvalidLoanApplicationException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                InvalidLoanApplicationException.INVALID_LOAN_APPLICATION,
                "Предварительная проверка не пройдена",
                ex.getErrors()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LoanCalculationException.class)
    public ResponseEntity<ErrorResponse> handleLoanCalculationException(LoanCalculationException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                LoanCalculationException.LOAN_CALCULATION_ERROR,
                "Ошибка расчета кредита: " + ex.getMessage(),
                Collections.emptyList()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidScoringDataException.class)
    public ResponseEntity<ErrorResponse> handleInvalidScoringData(InvalidScoringDataException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                InvalidScoringDataException.INVALID_SCORING_DATA,
                "Скоринг не пройден: ",
                ex.getErrors()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CreditDeclinedException.class)
    public ResponseEntity<ErrorResponse> handleCreditDeclinedException(CreditDeclinedException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                CreditDeclinedException.CREDIT_DECLINED_ERROR,
                "Заявка на кредит отклонена: ",
                ex.getErrors()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CreditCalculationException.class)
    public ResponseEntity<ErrorResponse> handleCreditCalculationException(CreditCalculationException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                CreditCalculationException.CREDIT_CALC_ERROR,
                "Ошибка расчета кредита: " + ex.getMessage(),
                Collections.emptyList()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConveyorFeignClientException.class)
    public ResponseEntity<ErrorResponse> handleConveyorFeignClientException(ConveyorFeignClientException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "CONVEYOR_CLIENT_ERROR",
                "Ошибка при взаимодействии с сервисом предложений",
                Collections.emptyList()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.SERVICE_UNAVAILABLE);
    }
}