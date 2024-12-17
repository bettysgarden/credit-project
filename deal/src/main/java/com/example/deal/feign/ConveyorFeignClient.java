package com.example.deal.feign;

import com.example.deal.model.dto.CreditResponse;
import com.example.deal.model.dto.LoanApplicationRequest;
import com.example.deal.model.dto.LoanOfferResponse;
import com.example.deal.model.dto.ScoringDataRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "ConveyorFeignClient",
        url = "${feign.client.conveyor.url}")
@Validated
public interface ConveyorFeignClient {
    @PostMapping("${feign.client.conveyor.path.offers}")
    List<LoanOfferResponse> conveyorOffersPost(@RequestBody @Validated LoanApplicationRequest loanApplicationRequestDTO);

    @PostMapping("${feign.client.conveyor.path.calculation}")
    CreditResponse conveyorCalculationPost(@RequestBody @Validated ScoringDataRequest scoringDataDTO);
}