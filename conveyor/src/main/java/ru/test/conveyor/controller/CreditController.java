package ru.test.conveyor.controller;

import com.example.credit.application.model.CreditDTO;
import com.example.credit.application.model.LoanApplicationRequestDTO;
import com.example.credit.application.model.LoanOfferDTO;
import com.example.credit.application.model.ScoringDataDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface CreditController {
    @PostMapping("/offers")
    ResponseEntity<List<LoanOfferDTO>> getOffersForClient(@RequestBody @Validated LoanApplicationRequestDTO loanApplicationRequestDTO);

    @PostMapping("/calculation")
    ResponseEntity<CreditDTO> getCalculation(@RequestBody @Validated ScoringDataDTO scoringDataDTO);

}
