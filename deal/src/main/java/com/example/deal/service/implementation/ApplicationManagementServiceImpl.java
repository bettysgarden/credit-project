package com.example.deal.service.implementation;

import com.example.deal.exception.DatabaseOperationException;
import com.example.deal.exception.InvalidLoanApplicationException;
import com.example.deal.model.dto.LoanApplicationRequest;
import com.example.deal.model.dto.LoanOfferRequest;
import com.example.deal.model.dto.LoanOfferResponse;
import com.example.deal.model.entity.ApplicationEntity;
import com.example.deal.model.entity.ClientEntity;
import com.example.deal.model.entity.CreditEntity;
import com.example.deal.model.entity.jsonb.AppliedLoanOffer;
import com.example.deal.model.enums.ApplicationStatusEnum;
import com.example.deal.repository.ApplicationRepository;
import com.example.deal.service.ApplicationManagementService;
import com.example.deal.service.ApplicationStatusService;
import com.example.deal.service.ClientService;
import com.example.deal.service.LoanOfferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApplicationManagementServiceImpl implements ApplicationManagementService {

    private final ApplicationRepository repository;
    private final ClientService clientService;
    private final LoanOfferService offerService;
    private final ApplicationStatusService applicationStatusService;

    @Override
    public List<LoanOfferResponse> createApplication(LoanApplicationRequest loanApplicationRequest) {
        log.info("Создание новой заявки для клиента: {}", loanApplicationRequest);

        ClientEntity clientEntity = clientService.createClient(loanApplicationRequest);
        ApplicationEntity applicationEntity = initializeApplication(clientEntity);

        try {
            return offerService.getLoanOffers(loanApplicationRequest, applicationEntity.getId());
        } catch (InvalidLoanApplicationException e) {
            log.error("Ошибка при запросе кредитных предложений для заявки ID: {}. Изменение статуса на CLIENT_DENIED.", applicationEntity.getId(), e);
            applicationStatusService.updateStatus(applicationEntity, ApplicationStatusEnum.CLIENT_DENIED);
            throw e;
        }
    }

    @Override
    public void setPickedLoanOffer(LoanOfferRequest loanOfferRequest) {
        ApplicationEntity applicationEntity = getApplicationById(loanOfferRequest.getApplicationId());

        AppliedLoanOffer appliedLoanOffer = offerService.setPickedLoanOffer(loanOfferRequest, applicationEntity);
        applyLoanOffer(applicationEntity, appliedLoanOffer);
    }

    private void applyLoanOffer(ApplicationEntity applicationEntity, AppliedLoanOffer loanOffer) {
        applicationEntity.setAppliedOffer(loanOffer);
        applicationStatusService.updateStatus(applicationEntity, ApplicationStatusEnum.DOCUMENT_CREATED);
        repository.save(applicationEntity);
        log.info("Кредитное предложение применено для заявки ID: {}", applicationEntity.getId());
    }

    private ApplicationEntity initializeApplication(ClientEntity clientEntity) {
        ApplicationEntity applicationEntity = new ApplicationEntity();
        applicationStatusService.updateStatus(applicationEntity, ApplicationStatusEnum.PREAPPROVAL);
        applicationEntity.setClient(clientEntity);
        applicationEntity.setCreationDate(LocalDateTime.now());
        repository.save(applicationEntity);
        log.info("Заявка создана: {}", applicationEntity);
        return applicationEntity;
    }

    @Override
    public ApplicationEntity getApplicationById(Long applicationId) {
        log.info("Получение заявки по ID: {}", applicationId);
        return repository.findById(applicationId)
                .orElseThrow(() -> {
                    log.error("Заявка с ID {} не найдена.", applicationId);
                    return new DatabaseOperationException("Заявка не найдена. ", List.of("ID заявки не найдено: " + applicationId));
                });
    }


    @Override
    public void setCredit(ApplicationEntity applicationEntity, CreditEntity creditEntity) {
        log.info("Установка кредита для заявки ID: {}", applicationEntity.getId());

        if (creditEntity == null) {
            log.info("Кредит отклонен для заявки ID: {}", applicationEntity.getId());
            applicationStatusService.updateStatus(applicationEntity, ApplicationStatusEnum.CC_DENIED);
        } else {
            log.info("Кредит одобрен для заявки ID: {}", applicationEntity.getId());
            applicationStatusService.updateStatus(applicationEntity, ApplicationStatusEnum.CC_APPROVED);
            applicationEntity.setCredit(creditEntity);
            applicationStatusService.updateStatus(applicationEntity, ApplicationStatusEnum.CREDIT_ISSUED);
        }
        repository.save(applicationEntity);
        log.info("Кредит обновлен для заявки ID: {}", applicationEntity.getId());
    }


}
