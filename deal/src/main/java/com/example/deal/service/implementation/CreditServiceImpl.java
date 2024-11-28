package com.example.deal.service.implementation;

import com.example.deal.exception.ConveyorFeignClientException;
import com.example.deal.exception.CreditDeclinedException;
import com.example.deal.exception.CreditServiceException;
import com.example.deal.exception.InvalidScoringDataException;
import com.example.deal.feign.ConveyorFeignClient;
import com.example.deal.model.dto.CreditResponse;
import com.example.deal.model.dto.FinishRegistrationRequest;
import com.example.deal.model.dto.ScoringDataRequest;
import com.example.deal.model.entity.ApplicationEntity;
import com.example.deal.model.entity.CreditEntity;
import com.example.deal.service.ApplicationManagementService;
import com.example.deal.service.ClientService;
import com.example.deal.service.CreditRepositoryAdapter;
import com.example.deal.service.CreditService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreditServiceImpl implements CreditService {

    private final ApplicationManagementService applicationService;
    private final ClientService clientService;
    private final CreditRepositoryAdapter creditRepositoryAdapter;
    private final ConveyorFeignClient conveyorFeignClient;

    @Override
    public void calculateCredit(Long applicationId, FinishRegistrationRequest finishRegistrationRequest) {

        ApplicationEntity applicationEntity = applicationService.getApplicationById(applicationId);
        ScoringDataRequest scoringInformation = clientService.getScoringInformation(applicationEntity, finishRegistrationRequest);

        CreditResponse creditResponse = getCreditResponseFromConveyor(applicationId, scoringInformation);
        try {
            CreditEntity creditEntity = creditRepositoryAdapter.saveCredit(creditResponse);
            applicationService.setCredit(applicationEntity, creditEntity);
        } catch (Exception e) {
            log.error("Ошибка при расчете кредита для Application ID: {}. Сообщение: {}", applicationId, e.getMessage());
            throw new CreditServiceException("Ошибка при расчете кредита для Application ID: " + applicationId, e);
        }
    }

    private CreditResponse getCreditResponseFromConveyor(Long applicationId, ScoringDataRequest scoringInformation) {
        try {
            log.info("Отправка запроса в ConveyorFeignClient для Application ID: {}", applicationId);
            CreditResponse creditResponse = conveyorFeignClient.conveyorCalculationPost(scoringInformation);
            log.debug("Получен ответ от ConveyorFeignClient: {}", creditResponse);
            return creditResponse;
        } catch (FeignException.BadRequest e) {
            log.error("Ошибка валидации данных через ConveyorFeignClient для Application ID: {}. Сообщение: {}", applicationId, e.getMessage());
            throw new InvalidScoringDataException("Ошибка при проверке данных через conveyor: ", List.of(e.getMessage()));
        } catch (FeignException.InternalServerError e) {
            log.error("Кредит отклонен через ConveyorFeignClient для Application ID: {}. Сообщение: {}", applicationId, e.getMessage());
            throw new CreditDeclinedException("Кредит отклонен: ", List.of(e.getMessage()));
        } catch (FeignException e) {
            log.error("Ошибка взаимодействия с ConveyorFeignClient для Application ID: {}. Сообщение: {}", applicationId, e.getMessage());
            throw new ConveyorFeignClientException("Ошибка при запросе предложений через FeignClient", e);
        }
    }
}
