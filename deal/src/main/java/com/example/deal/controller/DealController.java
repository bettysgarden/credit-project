package com.example.deal.controller;

import com.example.credit.application.api.DealApi;
import com.example.credit.application.model.FinishRegistrationRequestDTO;
import com.example.credit.application.model.LoanApplicationRequestDTO;
import com.example.credit.application.model.LoanOfferDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "deal")
@RestController
@RequestMapping("/deal")
@RequiredArgsConstructor
public class DealController implements DealApi {

    private final Logger logger = LoggerFactory.getLogger(DealController.class);

    @Override
    @Operation
    @PostMapping("/application")
    public ResponseEntity<List<LoanOfferDTO>> dealApplicationPost(
            @Parameter(name = "LoanApplicationRequestDTO", description = "Данные для расчёта кредита", required = true) @Valid @RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO) {
        logger.info("Получен запрос на расчет кредитных предложений. Параметры: {}", loanApplicationRequestDTO);

        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);

    }

    @Override
    @Operation
    @PutMapping("/calculate/{applicationId}")
    public ResponseEntity<Void> dealCalculateApplicationIdPut(
            @Parameter(name = "applicationId", description = "Идентификатор заявки", required = true, in = ParameterIn.PATH) @PathVariable("applicationId") Integer applicationId,
            @Parameter(name = "FinishRegistrationRequestDTO", description = "Данные для завершения регистрации", required = true) @Valid @RequestBody FinishRegistrationRequestDTO finishRegistrationRequestDTO
    ) {
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @Override
    @Operation
    @PutMapping("/offer")
    public ResponseEntity<Void> dealOfferPut(
            @Parameter(name = "LoanOfferDTO", description = "Выбранное кредитное предложение", required = true) @Valid @RequestBody LoanOfferDTO loanOfferDTO
    ) {
        return new ResponseEntity<>(HttpStatus.OK);

    }

}
