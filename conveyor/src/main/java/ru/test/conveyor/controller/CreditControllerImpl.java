package ru.test.conveyor.controller;

import com.example.credit.application.model.CreditDTO;
import com.example.credit.application.model.LoanApplicationRequestDTO;
import com.example.credit.application.model.LoanOfferDTO;
import com.example.credit.application.model.ScoringDataDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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
@RequiredArgsConstructor
public class CreditControllerImpl implements CreditController {

    private final Logger logger = LoggerFactory.getLogger(CreditControllerImpl.class);

    private final LoanService loanService;
    private final ScoringService scoringService;

    @Override
    @Operation
    @PostMapping("/calculation")
    public ResponseEntity<List<LoanOfferDTO>> getOffersForClient(@RequestBody @Validated LoanApplicationRequestDTO loanApplicationRequestDTO) {
        logger.info("Получен запрос на расчет кредитных предложений. Параметры: {}", loanApplicationRequestDTO);
        try {
            List<LoanOfferDTO> loanOffers = loanService.getLoanOffers(loanApplicationRequestDTO);
            logger.info("Сгенерированы {} кредитных предложений для клиента.", loanOffers.size());
            return ResponseEntity.ok(loanOffers);
        } catch (Exception e) {
            logger.error("Ошибка при расчете кредитных предложений: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    @Operation
    @PostMapping("/offers")
    public ResponseEntity<CreditDTO> getCalculation(@RequestBody @Validated ScoringDataDTO scoringDataDTO) {
        logger.info("Получен запрос на расчет кредита. Параметры: {}", scoringDataDTO);
        try {
            CreditDTO creditCalculation = scoringService.getCreditCalculation(scoringDataDTO);
            logger.info("Расчет кредита выполнен успешно. Результат: {}", creditCalculation);
            return ResponseEntity.ok(creditCalculation);
        } catch (Exception e) {
            logger.error("Ошибка при расчете кредита: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
