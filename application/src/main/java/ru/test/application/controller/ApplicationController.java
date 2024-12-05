package ru.test.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.test.application.model.dto.LoanApplicationRequest;
import ru.test.application.model.dto.LoanOfferRequest;
import ru.test.application.model.dto.LoanOfferResponse;
import ru.test.application.service.LoanService;

import java.util.List;

@Slf4j
@Tag(name = "application")
@RestController
@RequestMapping("/application")
@RequiredArgsConstructor
public class ApplicationController {
    private final LoanService loanService;

    @Operation
    @PutMapping("/offer")
    public ResponseEntity<Object> applicationOfferPut(@Parameter(name = "LoanOfferRequest", description = "Выбранное кредитное предложение", required = true) @Valid @RequestBody LoanOfferRequest loanOfferRequest) {
        log.info("Получен запрос на обновление заявки. Параметры: {}", loanOfferRequest);
        loanService.updateLoanOfferForApplication(loanOfferRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation
    @PostMapping
    public ResponseEntity<List<LoanOfferResponse>> applicationPost(@Parameter(name = "LoanApplicationRequestDTO", description = "Данные для расчёта кредита", required = true) @Valid @RequestBody LoanApplicationRequest loanApplicationRequest) {
        log.info("Получен запрос на расчет кредитных предложений. Параметры: {}", loanApplicationRequest);
        return new ResponseEntity<>(loanService.getLoanOffers(loanApplicationRequest), HttpStatus.OK);

    }
}
