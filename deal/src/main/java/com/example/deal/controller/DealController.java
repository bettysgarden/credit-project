package com.example.deal.controller;

import com.example.credit.application.model.FinishRegistrationRequestDTO;
import com.example.credit.application.model.LoanOfferDTO;
import com.example.deal.model.dto.LoanApplicationRequest;
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
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private OfferService offerService;
    @Autowired
    private ClientService clientService;

    @Operation
    @PostMapping("/application")
    public ResponseEntity<List<LoanOfferResponse>> dealApplicationPost(
            @Parameter(name = "LoanApplicationRequestDTO", description = "Данные для расчёта кредита", required = true) @Valid @RequestBody LoanApplicationRequest loanApplicationRequest) {
        log.info("Получен запрос на расчет кредитных предложений. Параметры: {}", loanApplicationRequest);

        // TODO Отправляется POST запрос на /conveyor/offers МС conveyor через FeignClient.
        List<LoanOfferResponse> loanOffers = offerService.getLoanOffers(loanApplicationRequest);

        // TODO На основе LoanApplicationRequestDTO создаётся сущность Client и сохраняется в БД.
        Long clientId = clientService.createClient(loanApplicationRequest);

        // TODO Создаётся Application со связью на только что созданный Client и сохраняется в БД.
        Long applicationId = applicationService.createApplication(loanApplicationRequest, clientId);

        // TODO Каждому элементу из списка List<LoanOfferDTO> присваивается id созданной заявки (Application)

        return new ResponseEntity<>(loanOffers, HttpStatus.OK);

    }

    @Operation
    @PutMapping("/calculate/{applicationId}")
    public ResponseEntity<Void> dealCalculateApplicationIdPut(
            @Parameter(name = "applicationId", description = "Идентификатор заявки", required = true, in = ParameterIn.PATH) @PathVariable("applicationId") Integer applicationId,
            @Parameter(name = "FinishRegistrationRequestDTO", description = "Данные для завершения регистрации", required = true) @Valid @RequestBody FinishRegistrationRequestDTO finishRegistrationRequestDTO
    ) {
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @Operation
    @PutMapping("/offer")
    public ResponseEntity<Void> dealOfferPut(
            @Parameter(name = "LoanOfferDTO", description = "Выбранное кредитное предложение", required = true) @Valid @RequestBody LoanOfferDTO loanOfferDTO
    ) {
        return new ResponseEntity<>(HttpStatus.OK);

    }

}
