package ru.test.application.service.impl;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.test.application.exception.DealFeignClientException;
import ru.test.application.exception.InvalidLoanApplicationException;
import ru.test.application.feign.DealFeignClient;
import ru.test.application.mapper.LoanApplicationMapper;
import ru.test.application.model.dto.LoanApplicationRequest;
import ru.test.application.model.dto.LoanOfferRequest;
import ru.test.application.model.dto.LoanOfferResponse;
import ru.test.application.model.entity.LoanApplication;
import ru.test.application.service.LoanService;
import ru.test.application.util.LoanApplicationValidator;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

    private final LoanApplicationValidator validator;
    private final LoanApplicationMapper mapper;
    private final DealFeignClient feignClient;

    @Override
    public List<LoanOfferResponse> getLoanOffers(LoanApplicationRequest loanApplicationDTO) {
        log.info("Начата обработка получения кредитных предложений для заявки: {}", loanApplicationDTO);

        prescoring(loanApplicationDTO);

        try {
            List<LoanOfferResponse> loanOfferResponses = feignClient.dealCreateApplicationPost(loanApplicationDTO);
            log.info("Кредитные предложения успешно получены: {}", loanOfferResponses);
            return loanOfferResponses;
        } catch (FeignException e) {
            log.error("Ошибка при вызове dealCreateApplicationPost: {}", e.getMessage(), e);
            throw new DealFeignClientException("Ошибка при запросе предложений через FeignClient.msa-deal", e);
        }
    }

    @Override
    public void updateLoanOfferForApplication(LoanOfferRequest loanOfferRequest) {
        log.info("Начато обновление кредитного предложения для заявки: {}", loanOfferRequest);

        try {
            feignClient.dealSetOfferApplicationPut(loanOfferRequest);
            log.info("Кредитное предложение успешно обновлено для заявки: {}", loanOfferRequest);
        } catch (FeignException e) {
            log.error("Ошибка при вызове dealSetOfferApplicationPut: {}", e.getMessage(), e);
            throw new DealFeignClientException("Ошибка при обновлении предложения через FeignClient", e);
        }
    }

    private void prescoring(LoanApplicationRequest loanApplicationDTO) {
        log.debug("Выполняется преобразование LoanApplicationRequest в LoanApplication");

        LoanApplication loanApplication = mapper.toEntity(loanApplicationDTO);

        log.debug("Валидация данных заявки: {}", loanApplication);

        List<String> validationErrors = validator.validate(loanApplication);

        if (!validationErrors.isEmpty()) {
            log.warn("Ошибка предварительной проверки: {}", validationErrors);
            throw new InvalidLoanApplicationException("Предварительная проверка не пройдена.", validationErrors);
        }

        log.info("Предварительная проверка заявки пройдена успешно.");
    }
}
