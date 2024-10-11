package ru.test.conveyor.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.test.conveyor.dto.CreditDTO;
import ru.test.conveyor.dto.LoanApplicationRequestDTO;
import ru.test.conveyor.dto.LoanOfferDTO;
import ru.test.conveyor.dto.ScoringDataDTO;

import java.util.List;

public interface CreditController {
    @PostMapping("/{offers}")
    ResponseEntity<List<LoanOfferDTO>> getOffersForClient(@RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO);

    @PostMapping("/{calculation}")
    ResponseEntity<CreditDTO> getCalculation(@RequestBody ScoringDataDTO scoringDataDTO);

}
