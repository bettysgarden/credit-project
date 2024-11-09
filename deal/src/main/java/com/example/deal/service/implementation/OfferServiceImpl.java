package com.example.deal.service.implementation;

import com.example.deal.exception.ConveyorFeignClientException;
import com.example.deal.exception.InvalidLoanApplicationException;
import com.example.deal.feign.ConveyorFeignClient;
import com.example.deal.model.dto.LoanApplicationRequest;
import com.example.deal.model.dto.LoanOfferResponse;
import com.example.deal.service.OfferService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OfferServiceImpl implements OfferService {
    private final ConveyorFeignClient conveyorFeignClient;

    @Override
    public List<LoanOfferResponse> getLoanOffers(LoanApplicationRequest loanApplicationRequestDTO) {
        List<LoanOfferResponse> loanOfferResponses;
        try {
            loanOfferResponses = conveyorFeignClient.conveyorOffers(loanApplicationRequestDTO);
        } catch (FeignException.BadRequest e) {
            throw new InvalidLoanApplicationException("Ошибка при проверке данных через conveyor: ", List.of(e.getMessage()));
        } catch (FeignException e) {
            throw new ConveyorFeignClientException("Ошибка при запросе предложений через FeignClient", e);
        }

        return loanOfferResponses;
    }
}
