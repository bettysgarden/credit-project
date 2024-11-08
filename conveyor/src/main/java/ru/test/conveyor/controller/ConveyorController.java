package ru.test.conveyor.controller;

import com.example.credit.application.api.ConveyorApi;
import com.example.credit.application.model.CreditDTO;
import com.example.credit.application.model.LoanApplicationRequestDTO;
import com.example.credit.application.model.LoanOfferDTO;
import com.example.credit.application.model.ScoringDataDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Tag(name = "conveyor")
@RestController
@RequestMapping("/conveyor")
@RequiredArgsConstructor
public class ConveyorController implements ConveyorApi {

    private final LoanService loanService;
    private final ScoringService scoringService;

    @Override
    @Operation
    @PostMapping("/offers")
    public ResponseEntity<List<LoanOfferDTO>> conveyorOffersPost(@RequestBody @Validated LoanApplicationRequestDTO loanApplicationRequestDTO) {
        log.info("Получен запрос на расчет кредитных предложений. Параметры: {}", loanApplicationRequestDTO);
        List<LoanOfferDTO> loanOffers = loanService.getLoanOffers(loanApplicationRequestDTO);
        log.info("Сгенерированы {} кредитных предложений для клиента.", loanOffers.size());
        return new ResponseEntity<>(loanOffers, HttpStatus.OK);
    }

    @Override
    @Operation
    @PostMapping("/calculation")
    public ResponseEntity<CreditDTO> conveyorCalculationPost(@RequestBody @Validated ScoringDataDTO scoringDataDTO) {
        log.info("Получен запрос на расчет кредита. Параметры: {}", scoringDataDTO);
        CreditDTO creditCalculation = scoringService.getCreditCalculation(scoringDataDTO);
        log.info("Расчет кредита выполнен успешно. Результат: {}", creditCalculation);
        return ResponseEntity.ok(creditCalculation);
    }
}