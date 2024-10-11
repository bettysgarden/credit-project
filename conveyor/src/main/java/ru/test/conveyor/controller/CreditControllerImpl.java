package ru.test.conveyor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.test.conveyor.dto.CreditDTO;
import ru.test.conveyor.dto.LoanApplicationRequestDTO;
import ru.test.conveyor.dto.LoanOfferDTO;
import ru.test.conveyor.dto.ScoringDataDTO;
import ru.test.conveyor.service.CreditService;

import java.util.List;

@Tag(name = "conveyor")
@RestController
@RequestMapping("/conveyor")
public class CreditControllerImpl implements CreditController {

    private final Logger logger = LoggerFactory.getLogger(CreditControllerImpl.class);

    @Autowired
    private CreditService creditService;

    @Override
    @Operation
    public ResponseEntity<List<LoanOfferDTO>> getOffersForClient(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        logger.info("getOffersForClient");
        return null;
    }

    @Override
    @Operation
    public ResponseEntity<CreditDTO> getCalculation(ScoringDataDTO scoringDataDTO) {
        logger.info("getCalculation");
        return null;
    }
}
