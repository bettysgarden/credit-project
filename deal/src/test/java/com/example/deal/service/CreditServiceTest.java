package com.example.deal.service;

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
import com.example.deal.service.implementation.LoanOfferServiceImpl;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreditServiceTest {
    @InjectMocks
    private LoanOfferServiceImpl loanService;
    @Mock
    private ConveyorFeignClient conveyorFeignClient;
    @Mock
    private LoanOfferMapper loanOfferMapper;

    @Test
    void testGetLoanOffers_Success() {
        LoanApplicationRequest request = new LoanApplicationRequest();
        Integer applicationId = 1;
        LoanOfferResponse response = new LoanOfferResponse();
        response.setApplicationId(null);
        List<LoanOfferResponse> expectedResponses = List.of(response);

        when(conveyorFeignClient.conveyorOffersPost(request)).thenReturn(expectedResponses);

        List<LoanOfferResponse> actualResponses = loanService.getLoanOffers(request, applicationId);

        assertNotNull(actualResponses);
        assertEquals(applicationId, actualResponses.get(0).getApplicationId());
        verify(conveyorFeignClient, times(1)).conveyorOffersPost(request);
    }

    @Test
    void testGetLoanOffers_FeignBadRequestException() {
        LoanApplicationRequest request = new LoanApplicationRequest();
        Integer applicationId = 1;
        FeignException.BadRequest feignException = mock(FeignException.BadRequest.class);
        when(feignException.getMessage()).thenReturn("Bad Request");

        when(conveyorFeignClient.conveyorOffersPost(request)).thenThrow(feignException);

        InvalidLoanApplicationException exception = assertThrows(InvalidLoanApplicationException.class,
                () -> loanService.getLoanOffers(request, applicationId));

        assertTrue(exception.getMessage().contains("Ошибка при проверке данных через conveyor"));
        verify(conveyorFeignClient, times(1)).conveyorOffersPost(request);
    }

    @Test
    void testGetLoanOffers_FeignException() {
        LoanApplicationRequest request = new LoanApplicationRequest();
        Integer applicationId = 1;
        FeignException feignException = mock(FeignException.class);

        when(conveyorFeignClient.conveyorOffersPost(request)).thenThrow(feignException);

        ConveyorFeignClientException exception = assertThrows(ConveyorFeignClientException.class,
                () -> loanService.getLoanOffers(request, applicationId));

        assertTrue(exception.getMessage().contains("Ошибка при запросе предложений через FeignClient"));
        verify(conveyorFeignClient, times(1)).conveyorOffersPost(request);
    }

    @Test
    void testSetPickedLoanOffer_Success() {
        LoanOfferRequest loanOfferRequest = new LoanOfferRequest();
        ApplicationEntity applicationEntity = new ApplicationEntity();
        applicationEntity.setAppliedOffer(null);

        AppliedLoanOffer expectedAppliedOffer = new AppliedLoanOffer();
        when(loanOfferMapper.toJson(loanOfferRequest)).thenReturn(expectedAppliedOffer);

        AppliedLoanOffer actualAppliedOffer = loanService.setPickedLoanOffer(loanOfferRequest, applicationEntity);

        assertEquals(expectedAppliedOffer, actualAppliedOffer);
        verify(loanOfferMapper, times(1)).toJson(loanOfferRequest);
    }

    @Test
    void testSetPickedLoanOffer_StateTransitionException() {
        LoanOfferRequest loanOfferRequest = new LoanOfferRequest();
        ApplicationEntity applicationEntity = new ApplicationEntity();
        applicationEntity.setAppliedOffer(new AppliedLoanOffer());

        StateTransitionException exception = assertThrows(StateTransitionException.class,
                () -> loanService.setPickedLoanOffer(loanOfferRequest, applicationEntity));

        assertTrue(exception.getMessage().contains("Некорректный выбор кредитного предложения."));
    }
}
