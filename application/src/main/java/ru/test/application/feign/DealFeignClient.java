package ru.test.application.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.test.application.model.dto.LoanApplicationRequest;
import ru.test.application.model.dto.LoanOfferRequest;
import ru.test.application.model.dto.LoanOfferResponse;

import java.util.List;

@FeignClient(name = "DealFeignClient", url = "${feign.client.deal.url}")
@Validated
public interface DealFeignClient {
    @PostMapping("${feign.client.deal.path.application}")
    List<LoanOfferResponse> dealCreateApplicationPost(@RequestBody @Validated LoanApplicationRequest loanApplicationRequestDTO);

    @PutMapping("${feign.client.deal.path.offer}")
    void dealSetOfferApplicationPut(@RequestBody @Validated LoanOfferRequest loanOfferRequest);
}