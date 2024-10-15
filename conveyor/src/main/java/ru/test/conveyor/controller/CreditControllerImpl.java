package ru.test.conveyor.controller;

import com.example.credit.application.model.CreditDTO;
import com.example.credit.application.model.LoanApplicationRequestDTO;
import com.example.credit.application.model.LoanOfferDTO;
import com.example.credit.application.model.ScoringDataDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.test.conveyor.service.LoanService;
import ru.test.conveyor.service.ScoringService;

import java.util.List;

@Tag(name = "conveyor")
@RestController
@RequestMapping("/conveyor")
public class CreditControllerImpl implements CreditController {

    private final Logger logger = LoggerFactory.getLogger(CreditControllerImpl.class);

    private final LoanService loanService;
    private final ScoringService scoringService;

    public CreditControllerImpl(LoanService loanService, ScoringService scoringService) {
        this.loanService = loanService;
        this.scoringService = scoringService;
    }

    @Override
    @Operation
    @PostMapping("/calculation")
    public ResponseEntity<List<LoanOfferDTO>> getOffersForClient(@RequestBody @Validated LoanApplicationRequestDTO loanApplicationRequestDTO) {
        logger.info("getOffersForClient");
        List<LoanOfferDTO> loanOffers = loanService.getLoanOffers(loanApplicationRequestDTO);
        return ResponseEntity.ok(loanOffers);
    }

    @Override
    @Operation
    @PostMapping("/offers")
    public ResponseEntity<CreditDTO> getCalculation(@RequestBody @Validated ScoringDataDTO scoringDataDTO) {
        logger.info("getCalculation");
        CreditDTO creditCalculation = scoringService.getCreditCalculation(scoringDataDTO);
        return ResponseEntity.ok(creditCalculation);
    }
}
