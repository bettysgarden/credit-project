package com.example.deal.service.implementation;

import com.example.deal.exception.ConveyorFeignClientException;
import com.example.deal.exception.InvalidLoanApplicationException;
import com.example.deal.exception.StateTransitionException;
import com.example.deal.feign.ConveyorFeignClient;
import com.example.deal.mapper.LoanOfferMapper;
import com.example.deal.model.dto.LoanApplicationRequest;
import com.example.deal.model.dto.LoanOfferRequest;
import com.example.deal.model.dto.LoanOfferResponse;
import com.example.deal.model.entity.ApplicationEntity;
import com.example.deal.model.entity.jsonb.AppliedLoanOffer;
import com.example.deal.service.LoanOfferService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoanOfferServiceImpl implements LoanOfferService {
    private final ConveyorFeignClient conveyorFeignClient;
    private final LoanOfferMapper loanOfferMapper;

    @Override
    public List<LoanOfferResponse> getLoanOffers(LoanApplicationRequest loanApplicationRequestDTO, Integer applicationId) {
        List<LoanOfferResponse> loanOfferResponses;
        try {
            loanOfferResponses = conveyorFeignClient.conveyorOffersPost(loanApplicationRequestDTO);
        } catch (FeignException.BadRequest e) {
            log.warn("Ошибка при проверке данных через conveyor: {}", List.of(e.getMessage()));
            throw new InvalidLoanApplicationException("Ошибка при проверке данных через conveyor: ", List.of(e.getMessage()));
        } catch (FeignException e) {
            log.warn("Ошибка при запросе предложений через FeignClient{}", String.valueOf(e));
            throw new ConveyorFeignClientException("Ошибка при запросе предложений через FeignClient", e);
        }

        loanOfferResponses.forEach(offer -> offer.setApplicationId(applicationId));
        return loanOfferResponses;
    }

    @Override
    public AppliedLoanOffer setPickedLoanOffer(LoanOfferRequest loanOfferRequest, ApplicationEntity applicationEntity) {
        log.info("Выбор кредитного предложения для заявки ID: {}", loanOfferRequest.getApplicationId());
        validateOfferNotPicked(applicationEntity);
        return loanOfferMapper.toJson(loanOfferRequest);
    }

    private void validateOfferNotPicked(ApplicationEntity applicationEntity) {
        if (applicationEntity.getAppliedOffer() != null) {
            log.warn("Для заявки ID {} уже выбрано кредитное предложение.", applicationEntity.getId());
            throw new StateTransitionException("Некорректный выбор кредитного предложения. ",
                    List.of("Для данной заявки уже выбрано кредитное предложение: " + applicationEntity.getId()));
        }
    }

}
