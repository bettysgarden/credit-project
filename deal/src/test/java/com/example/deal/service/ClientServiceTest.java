package com.example.deal.service;

import com.example.deal.exception.ClientServiceException;
import com.example.deal.exception.InvalidScoringDataException;
import com.example.deal.model.dto.FinishRegistrationRequest;
import com.example.deal.model.dto.LoanApplicationRequest;
import com.example.deal.model.dto.ScoringDataRequest;
import com.example.deal.model.entity.ApplicationEntity;
import com.example.deal.model.entity.ClientEntity;
import com.example.deal.model.entity.PassportEntity;
import com.example.deal.model.entity.jsonb.AppliedLoanOffer;
import com.example.deal.model.enums.GenderEnum;
import com.example.deal.repository.ClientRepository;
import com.example.deal.repository.PassportRepository;
import com.example.deal.service.implementation.ClientServiceImpl;
import com.example.deal.utils.PassportValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {
    @InjectMocks
    private ClientServiceImpl clientService;
    @Mock
    private ClientRepository repository;
    @Mock
    private PassportRepository passportRepository;
    @Mock
    private PassportValidator passportValidator;

    @Test
    void createClient_Success() {
        LoanApplicationRequest request = new LoanApplicationRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPassportSeries("1234");
        request.setPassportNumber("567890");

        PassportEntity passport = new PassportEntity();
        passport.setSeries(request.getPassportSeries());
        passport.setNumber(request.getPassportNumber());

        ClientEntity savedClient = new ClientEntity();
        savedClient.setFirstName(request.getFirstName());
        savedClient.setLastName(request.getLastName());
        savedClient.setPassport(passport);

        when(passportValidator.validatePassport(any())).thenReturn(List.of());
        when(passportRepository.save(any(PassportEntity.class))).thenReturn(passport);
        when(repository.save(any(ClientEntity.class))).thenReturn(savedClient);

        ClientEntity result = clientService.createClient(request);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        verify(passportRepository).save(any(PassportEntity.class));
        verify(repository).save(any(ClientEntity.class));
    }

    @Test
    void createClient_InvalidPassport() {
        LoanApplicationRequest request = new LoanApplicationRequest();
        request.setFirstName("John");
        request.setPassportSeries("1234");
        request.setPassportNumber("567890");

        List<String> validationErrors = List.of("Invalid passport series");
        when(passportValidator.validatePassport(any())).thenReturn(validationErrors);

        InvalidScoringDataException exception = assertThrows(
                InvalidScoringDataException.class,
                () -> clientService.createClient(request)
        );

        assertTrue(exception.getErrors().contains("Invalid passport series"));
        verifyNoInteractions(passportRepository, repository);
    }

    @Test
    void createClient_ExceptionDuringSave() {
        LoanApplicationRequest request = new LoanApplicationRequest();
        request.setFirstName("John");
        request.setPassportSeries("1234");
        request.setPassportNumber("567890");

        PassportEntity passport = new PassportEntity();
        passport.setSeries(request.getPassportSeries());
        passport.setNumber(request.getPassportNumber());

        when(passportValidator.validatePassport(any())).thenReturn(List.of());
        when(passportRepository.save(any(PassportEntity.class))).thenReturn(passport);
        when(repository.save(any(ClientEntity.class))).thenThrow(new RuntimeException("Database error"));

        ClientServiceException exception = assertThrows(
                ClientServiceException.class,
                () -> clientService.createClient(request)
        );

        assertTrue(exception.getMessage().contains("Ошибка при создании клиента"));
        verify(passportRepository).save(any(PassportEntity.class));
        verify(repository).save(any(ClientEntity.class));
    }

    @Test
    void getScoringInformation_Success() {
        ApplicationEntity application = new ApplicationEntity();
        application.setId(1);

        ClientEntity client = new ClientEntity();
        client.setFirstName("John");
        client.setLastName("Doe");
        PassportEntity passport = new PassportEntity();
        passport.setSeries("1234");
        passport.setNumber("567890");
        client.setPassport(passport);
        application.setClient(client);

        AppliedLoanOffer offer = new AppliedLoanOffer();
        offer.setTotalAmount(BigDecimal.valueOf(100000.0));
        offer.setTerm(12);
        application.setAppliedOffer(offer);

        FinishRegistrationRequest finishRequest = new FinishRegistrationRequest();
        finishRequest.setGender(GenderEnum.MALE);
        finishRequest.setPassportIssueDate(LocalDate.now());

        ScoringDataRequest result = clientService.getScoringInformation(application, finishRequest);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("1234", result.getPassportSeries());
    }

    @Test
    void getScoringInformation_ClientMissing() {
        ApplicationEntity application = new ApplicationEntity();
        application.setId(1);

        FinishRegistrationRequest finishRequest = new FinishRegistrationRequest();

        ClientServiceException exception = assertThrows(
                ClientServiceException.class,
                () -> clientService.getScoringInformation(application, finishRequest)
        );

        assertTrue(exception.getMessage().contains("Клиент отсутствует"));
    }

    @Test
    void getScoringInformation_AppliedOfferMissing() {
        ApplicationEntity application = new ApplicationEntity();
        application.setId(1);

        ClientEntity client = new ClientEntity();
        application.setClient(client);

        FinishRegistrationRequest finishRequest = new FinishRegistrationRequest();

        ClientServiceException exception = assertThrows(
                ClientServiceException.class,
                () -> clientService.getScoringInformation(application, finishRequest)
        );

        assertTrue(exception.getMessage().contains("не задано кредитное предложение"));
    }

}
