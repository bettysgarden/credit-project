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

@FeignClient(value = "${feign.client.value}", url = "${feign.client.url}", path = "${feign.client.path}")
@Validated
public interface ConveyorFeignClient {
    @PostMapping("/calculation")
    List<LoanOfferResponse> conveyorOffers(@RequestBody @Validated LoanApplicationRequest loanApplicationRequestDTO);

    @PostMapping("/offers")
    CreditResponse conveyorCalculationPost(@RequestBody @Validated ScoringDataRequest scoringDataDTO);
}