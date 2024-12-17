package com.example.deal.service;

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
import com.example.deal.service.implementation.ApplicationManagementServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ApplicationManagementServiceTest {
    @InjectMocks
    private ApplicationManagementServiceImpl applicationManagementService;
    @Mock
    private ApplicationRepository repository;
    @Mock
    private ClientService clientService;
    @Mock
    private LoanOfferService offerService;
    @Mock
    private ApplicationStatusService applicationStatusService;

    private LoanApplicationRequest loanApplicationRequest;
    private ApplicationEntity applicationEntity;
    private ClientEntity clientEntity;

    @BeforeEach
    void setUp() {
        loanApplicationRequest = new LoanApplicationRequest();
        loanApplicationRequest.setFirstName("John");
        loanApplicationRequest.setLastName("Doe");

        clientEntity = new ClientEntity();
        clientEntity.setId(1);

        applicationEntity = new ApplicationEntity();
        applicationEntity.setId(1);
        applicationEntity.setClient(clientEntity);
        applicationEntity.setCreationDate(LocalDateTime.now());
    }

    @Test
    void testCreateApplication_Success() {
        List<LoanOfferResponse> mockOffers = List.of(new LoanOfferResponse());
        when(clientService.createClient(loanApplicationRequest)).thenReturn(clientEntity);
        when(repository.save(any(ApplicationEntity.class))).thenAnswer(invocation -> {
            ApplicationEntity savedEntity = invocation.getArgument(0);
            savedEntity.setId(1);
            return savedEntity;
        });
        when(offerService.getLoanOffers(eq(loanApplicationRequest), anyInt())).thenReturn(mockOffers);

        List<LoanOfferResponse> offers = applicationManagementService.createApplication(loanApplicationRequest);

        assertNotNull(offers);
        assertEquals(1, offers.size());
        verify(applicationStatusService).updateStatus(any(ApplicationEntity.class), eq(ApplicationStatusEnum.PREAPPROVAL));
        verify(repository).save(any(ApplicationEntity.class));
    }

    @Test
    void testCreateApplication_InvalidLoanApplication() {
        when(clientService.createClient(loanApplicationRequest)).thenReturn(clientEntity);
        when(repository.save(any(ApplicationEntity.class))).thenAnswer(invocation -> {
            ApplicationEntity savedEntity = invocation.getArgument(0);
            savedEntity.setId(1);
            return savedEntity;
        });
        when(offerService.getLoanOffers(loanApplicationRequest, applicationEntity.getId()))
                .thenThrow(new InvalidLoanApplicationException("Invalid loan application",
                        Collections.emptyList()));

        assertThrows(InvalidLoanApplicationException.class,
                () -> applicationManagementService.createApplication(loanApplicationRequest));

        verify(applicationStatusService).updateStatus(any(ApplicationEntity.class), eq(ApplicationStatusEnum.CLIENT_DENIED));
    }

    @Test
    void testSetPickedLoanOffer_Success() {
        LoanOfferRequest loanOfferRequest = new LoanOfferRequest();
        loanOfferRequest.setApplicationId(1L);

        AppliedLoanOffer appliedLoanOffer = new AppliedLoanOffer();
        when(repository.findById(1L)).thenReturn(Optional.of(applicationEntity));
        when(offerService.setPickedLoanOffer(loanOfferRequest, applicationEntity)).thenReturn(appliedLoanOffer);

        applicationManagementService.setPickedLoanOffer(loanOfferRequest);

        verify(applicationStatusService).updateStatus(applicationEntity, ApplicationStatusEnum.DOCUMENT_CREATED);
        verify(repository).save(applicationEntity);
    }

    @Test
    void testGetApplicationById_Success() {
        when(repository.findById(1L)).thenReturn(Optional.of(applicationEntity));

        ApplicationEntity result = applicationManagementService.getApplicationById(1L);

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(repository).findById(1L);
    }

    @Test
    void testGetApplicationById_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        DatabaseOperationException exception = assertThrows(DatabaseOperationException.class,
                () -> applicationManagementService.getApplicationById(1L));

        assertTrue(exception.getMessage().contains("Заявка не найдена"));
        verify(repository).findById(1L);
    }

    @Test
    void testSetCredit_Success() {
        CreditEntity creditEntity = new CreditEntity();
        when(repository.save(applicationEntity)).thenReturn(applicationEntity);

        applicationManagementService.setCredit(applicationEntity, creditEntity);

        verify(applicationStatusService).updateStatus(applicationEntity, ApplicationStatusEnum.CC_APPROVED);
        verify(applicationStatusService).updateStatus(applicationEntity, ApplicationStatusEnum.CREDIT_ISSUED);
        verify(repository).save(applicationEntity);
    }

    @Test
    void testSetCredit_NullCreditEntity() {
        applicationManagementService.setCredit(applicationEntity, null);

        verify(applicationStatusService).updateStatus(applicationEntity, ApplicationStatusEnum.CC_DENIED);
        verify(repository).save(applicationEntity);
    }
}
