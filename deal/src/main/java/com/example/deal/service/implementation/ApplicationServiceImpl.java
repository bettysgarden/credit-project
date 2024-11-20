package com.example.deal.service.implementation;

import com.example.deal.mapper.LoanOfferMapper;
import com.example.deal.model.dto.LoanApplicationRequest;
import com.example.deal.model.dto.LoanOfferRequest;
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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository repository;
    private final ApplicationStatusRepository statusRepository;
    private final LoanOfferMapper loanOfferMapper;
    private final ClientService clientService;

    @Override
    public Long createApplication(LoanApplicationRequest loanApplicationRequest, Long clientId) {
        ApplicationStatusEntity status = statusRepository.findByTitle(ApplicationStatusEnum.PREAPPROVAL)
                .orElseThrow(() -> new EntityNotFoundException("Status not found: PREAPPROVAL"));

        ApplicationEntity applicationEntity = new ApplicationEntity();
        applicationEntity.setClientId(clientService.getClient(clientId));
        // TODO обработать исключение
        applicationEntity.setCreationDate(LocalDateTime.now());
        applicationEntity.setStatus(status);
        applicationEntity.setStatusHistory(updateStatusHistory(applicationEntity));
        repository.save(applicationEntity);

        return applicationEntity.getId();
    }


    @Override
    public void updateApplication(LoanOfferRequest loanOfferRequest) {

                /*
         TODO
            обработка исключений
         */

        ApplicationEntity applicationEntity = repository.findById(loanOfferRequest.getApplicationId())
                .orElseThrow(() -> new EntityNotFoundException("Application not found with ID: " + loanOfferRequest.getApplicationId()));
        applicationEntity.setAppliedOffer(loanOfferMapper.toJson(loanOfferRequest));

        ApplicationStatusEntity status = statusRepository.findByTitle(ApplicationStatusEnum.APPROVED)
                .orElseThrow(() -> new EntityNotFoundException("Status not found: APPROVED"));
        applicationEntity.setStatus(status);
        applicationEntity.setStatusHistory(updateStatusHistory(applicationEntity));
        // TODO исключения

        repository.save(applicationEntity);

    }

    private static List<StatusHistory> updateStatusHistory(ApplicationEntity applicationEntity) {
        List<StatusHistory> history = applicationEntity.getStatusHistory();
        if (history == null) {
            history = new ArrayList<>();
        }
        boolean statusAlreadyExists = history.stream()
                .anyMatch(entry -> entry.getStatus().equals(applicationEntity.getStatus().getTitle()));

        if (statusAlreadyExists) {
            throw new IllegalArgumentException("Status " + applicationEntity.getStatus().getTitle() + " already exists in the history.");
        }
        StatusHistory statusHistory = new StatusHistory();
        statusHistory.setStatus(applicationEntity.getStatus().getTitle());
        statusHistory.setTime(applicationEntity.getCreationDate());
        statusHistory.setChangeType(ChangeTypeEnum.AUTO);
        history.add(statusHistory);

        return history;
    }
}
