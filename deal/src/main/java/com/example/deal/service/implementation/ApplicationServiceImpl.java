package com.example.deal.service.implementation;

import com.example.deal.model.dto.LoanApplicationRequest;
import com.example.deal.model.entity.ApplicationEntity;
import com.example.deal.model.entity.ApplicationStatusEntity;
import com.example.deal.model.entity.jsonb.StatusHistory;
import com.example.deal.model.enums.ApplicationStatusEnum;
import com.example.deal.model.enums.ChangeTypeEnum;
import com.example.deal.repository.ApplicationRepository;
import com.example.deal.repository.ApplicationStatusRepository;
import com.example.deal.service.ApplicationService;
import com.example.deal.service.ClientService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/*
ApplicationService: реализует логику обработки и сохранения сущности Application,
включая создание новой заявки, присвоение статуса, и обновление истории статусов.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository repository;
    private final ApplicationStatusRepository statusRepository;

    @Autowired
    private ClientService clientService;

    @Override
    public Long createApplication(LoanApplicationRequest loanApplicationRequest, Long clientId) {
        ApplicationStatusEntity status = statusRepository.findByTitle(ApplicationStatusEnum.PREAPPROVAL)
                .orElseThrow(() -> new EntityNotFoundException("Status not found: PREAPPROVAL"));

        ApplicationEntity applicationEntity = new ApplicationEntity();
        applicationEntity.setClientId(clientService.getClient(clientId));
        // TODO обработать исключение
        applicationEntity.setCreationDate(LocalDateTime.now());
        applicationEntity.setStatus(status);

        StatusHistory statusHistory = new StatusHistory();
        statusHistory.setStatus(status.getTitle());
        statusHistory.setTime(applicationEntity.getCreationDate());
        statusHistory.setChangeType(ChangeTypeEnum.AUTO);

        applicationEntity.setStatusHistory(statusHistory);
        repository.save(applicationEntity);

        return applicationEntity.getId();
    }
}
