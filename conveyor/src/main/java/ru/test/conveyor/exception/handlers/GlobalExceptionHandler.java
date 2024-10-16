package ru.test.conveyor.exception.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.test.conveyor.exception.CreditDeclinedException;
import ru.test.conveyor.exception.InvalidLoanApplicationException;
import ru.test.conveyor.exception.InvalidScoringDataException;
import ru.test.conveyor.exception.LoanCalculationException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InvalidLoanApplicationException.class)
    public ResponseEntity<String> handleInvalidLoanApplication(InvalidLoanApplicationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LoanCalculationException.class)
    public ResponseEntity<String> handleLoanCalculationException(LoanCalculationException ex) {
        return new ResponseEntity<>("Ошибка расчета кредита: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidScoringDataException.class)
    public ResponseEntity<String> handleInvalidScoringData(InvalidScoringDataException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CreditDeclinedException.class)
    public ResponseEntity<String> handleCreditDeclinedException(CreditDeclinedException ex) {
        return new ResponseEntity<>("Ошибка расчета кредита: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}