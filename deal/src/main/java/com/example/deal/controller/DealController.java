package com.example.deal.controller;

import com.example.deal.model.dto.FinishRegistrationRequest;
import com.example.deal.model.dto.LoanApplicationRequest;
import com.example.deal.model.dto.LoanOfferRequest;
import com.example.deal.model.dto.LoanOfferResponse;
import com.example.deal.service.ApplicationService;
import com.example.deal.service.ClientService;
import com.example.deal.service.OfferService;
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

    private final ApplicationService applicationService;
    private final OfferService offerService;
    private final ClientService clientService;

    @Operation
    @PostMapping("/application")
    public ResponseEntity<List<LoanOfferResponse>> dealApplicationPost(
            @Parameter(name = "LoanApplicationRequestDTO", description = "Данные для расчёта кредита", required = true) @Valid @RequestBody LoanApplicationRequest loanApplicationRequest) {
        log.info("Получен запрос на расчет кредитных предложений. Параметры: {}", loanApplicationRequest);

        List<LoanOfferResponse> loanOffers = offerService.getLoanOffers(loanApplicationRequest);
        Long clientId = clientService.createClient(loanApplicationRequest);
        Long applicationId = applicationService.createApplication(loanApplicationRequest, clientId);
        loanOffers.forEach(offer -> offer.setApplicationId(applicationId));

        return new ResponseEntity<>(loanOffers, HttpStatus.OK);

    }

    @Operation
    @PutMapping("/calculate/{applicationId}")
    public ResponseEntity<Void> dealCalculateApplicationIdPut(
            @Parameter(name = "applicationId", description = "Идентификатор заявки", required = true, in = ParameterIn.PATH) @PathVariable("applicationId") Integer applicationId,
            @Parameter(name = "FinishRegistrationRequestDTO", description = "Данные для завершения регистрации", required = true) @Valid @RequestBody FinishRegistrationRequest finishRegistrationRequest
    ) {
        // TODO

        return new ResponseEntity<>(HttpStatus.OK);

    }

    @Operation
    @PutMapping("/offer")
    public ResponseEntity<Void> dealOfferPut(
            @Parameter(name = "LoanOfferRequest", description = "Выбранное кредитное предложение", required = true)
            @Valid @RequestBody LoanOfferRequest loanOfferRequest
    ) {
        applicationService.updateApplication(loanOfferRequest);
        return new ResponseEntity<>(HttpStatus.OK);

    }

}
