package com.example.deal.controller;

import com.example.deal.model.dto.FinishRegistrationRequest;
import com.example.deal.model.dto.LoanApplicationRequest;
import com.example.deal.model.dto.LoanOfferRequest;
import com.example.deal.model.dto.LoanOfferResponse;
import com.example.deal.service.ApplicationManagementService;
import com.example.deal.service.CreditService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "deal")
@RestController
@RequestMapping("/deal")
@RequiredArgsConstructor
@Slf4j
public class DealController {

    private final ApplicationManagementService applicationService;
    private final CreditService creditService;

    @Operation
    @PostMapping("/application")
    public ResponseEntity<List<LoanOfferResponse>> dealApplicationPost(
            @Parameter(name = "LoanApplicationRequestDTO", description = "Данные для расчёта кредита", required = true) @Valid @RequestBody LoanApplicationRequest loanApplicationRequest) {
        log.info("Получен запрос на расчет кредитных предложений. Параметры: {}", loanApplicationRequest);
        List<LoanOfferResponse> loanOffers = applicationService.createApplication(loanApplicationRequest);
        return new ResponseEntity<>(loanOffers, HttpStatus.OK);
    }

    @Operation
    @PutMapping("/calculate/{applicationId}")
    public ResponseEntity<String> dealCalculateApplicationIdPut(
            @Parameter(name = "applicationId", description = "Идентификатор заявки", required = true, in = ParameterIn.PATH) @PathVariable("applicationId") Long applicationId,
            @Parameter(name = "FinishRegistrationRequestDTO", description = "Данные для завершения регистрации", required = true) @Valid @RequestBody FinishRegistrationRequest finishRegistrationRequest
    ) {
        creditService.calculateCredit(applicationId, finishRegistrationRequest);
        return ResponseEntity.ok("Заявка успешно обновлена. ");
    }

    @Operation
    @PutMapping("/offer")
    public ResponseEntity<String> dealOfferPut(
            @Parameter(name = "LoanOfferRequest", description = "Выбранное кредитное предложение", required = true)
            @Valid @RequestBody LoanOfferRequest loanOfferRequest
    ) {
        applicationService.setPickedLoanOffer(loanOfferRequest);
        return ResponseEntity.ok("Заявка успешно обновлена. ");

    }

}
