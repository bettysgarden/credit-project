package com.example.deal.service.implementation;

import com.example.deal.exception.ClientServiceException;
import com.example.deal.exception.InvalidScoringDataException;
import com.example.deal.model.dto.FinishRegistrationRequest;
import com.example.deal.model.dto.LoanApplicationRequest;
import com.example.deal.model.dto.ScoringDataRequest;
import com.example.deal.model.entity.ApplicationEntity;
import com.example.deal.model.entity.ClientEntity;
import com.example.deal.model.entity.PassportEntity;
import com.example.deal.repository.ClientRepository;
import com.example.deal.repository.PassportRepository;
import com.example.deal.service.ClientService;
import com.example.deal.utils.PassportValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientRepository repository;
    private final PassportRepository passportRepository;
    private final PassportValidator passportValidator;

    private static ScoringDataRequest getScoringDataRequest(ApplicationEntity application, FinishRegistrationRequest finishRegistrationRequest, ClientEntity client) {
        ScoringDataRequest scoringDataRequest = new ScoringDataRequest();
        scoringDataRequest.setFirstName(client.getFirstName());
        scoringDataRequest.setMiddleName(client.getMiddleName());
        scoringDataRequest.setLastName(client.getLastName());
        scoringDataRequest.setGender(finishRegistrationRequest.getGender());
        scoringDataRequest.setBirthdate(client.getBirthDate());
        scoringDataRequest.setPassportSeries(client.getPassport().getSeries());
        scoringDataRequest.setPassportNumber(client.getPassport().getNumber());
        scoringDataRequest.setPassportIssueDate(finishRegistrationRequest.getPassportIssueDate());
        scoringDataRequest.setPassportIssueBranch(finishRegistrationRequest.getPassportIssueBranch());
        scoringDataRequest.setMaritalStatus(finishRegistrationRequest.getMaritalStatus());
        scoringDataRequest.setDependentAmount(finishRegistrationRequest.getDependentAmount());
        scoringDataRequest.setEmployment(finishRegistrationRequest.getEmploymentDTO());
        scoringDataRequest.setAccount(finishRegistrationRequest.getAccount());
        scoringDataRequest.setIsSalaryClient(application.getAppliedOffer().getIsSalaryClient());
        scoringDataRequest.setIsInsuranceEnabled(application.getAppliedOffer().getIsInsuranceEnabled());
        scoringDataRequest.setAmount(application.getAppliedOffer().getTotalAmount());
        scoringDataRequest.setTerm(application.getAppliedOffer().getTerm());
        return scoringDataRequest;
    }

    @Override
    public ClientEntity createClient(LoanApplicationRequest loanApplicationRequest) {
        log.debug("Начало создания клиента с данными: {}", loanApplicationRequest);
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setFirstName(loanApplicationRequest.getFirstName());
        clientEntity.setMiddleName(loanApplicationRequest.getMiddleName());
        clientEntity.setLastName(loanApplicationRequest.getLastName());
        clientEntity.setEmail(loanApplicationRequest.getEmail());
        clientEntity.setBirthDate(loanApplicationRequest.getBirthdate());

        PassportEntity passport = new PassportEntity();
        passport.setSeries(loanApplicationRequest.getPassportSeries());
        passport.setNumber(loanApplicationRequest.getPassportNumber());
        List<String> validationErrors = passportValidator.validatePassport(passport);
        if (!validationErrors.isEmpty()) {
            log.error("Ошибка валидации паспорта: {}", validationErrors);
            throw new InvalidScoringDataException("Ошибка валидации паспорта", validationErrors);
        }

        try {
            passportRepository.save(passport);
            log.debug("Паспорт сохранён: {}", passport);

            clientEntity.setPassport(passport);
            repository.save(clientEntity);
            log.info("Клиент успешно создан: {}", clientEntity);

            return clientEntity;
        } catch (Exception e) {
            log.error("Ошибка при создании клиента: {}", e.getMessage(), e);
            throw new ClientServiceException("Ошибка при создании клиента. : ", e);
        }
    }

    @Override
    public ClientEntity getClient(Long clientId) {
        log.debug("Получение клиента с ID: {}", clientId);

        return repository.findById(clientId)
                .orElseThrow(() -> {
                    log.error("Клиент с ID {} не найден", clientId);
                    return new ClientServiceException("Клиент с ID не найден: " + clientId);
                });
    }

    @Override
    public ScoringDataRequest getScoringInformation(ApplicationEntity application, FinishRegistrationRequest finishRegistrationRequest) {
        log.debug("Начало формирования ScoringDataRequest для application: {}", application.getId());

        ClientEntity client = application.getClient();

        if (client == null) {
            log.error("Клиент отсутствует в заявке с ID: {}", application.getId());
            throw new ClientServiceException("Клиент отсутствует в заявке.");
        }
        if (application.getAppliedOffer() == null) {
            log.error("Для заявки с ID не задано кредитное предложение: {}", application.getId());
            throw new ClientServiceException("Для заявки с ID не задано кредитное предложение: " + application.getId());
        }
        try {
            ScoringDataRequest scoringDataRequest = getScoringDataRequest(application, finishRegistrationRequest, client);

            log.info("ScoringDataRequest успешно сформирован для application ID: {}", application.getId());
            return scoringDataRequest;
        } catch (Exception e) {
            log.error("Ошибка при формировании ScoringDataRequest для application ID: {}", application.getId(), e);
            throw new ClientServiceException("Ошибка формирования ScoringDataRequest", e);
        }
    }
}
