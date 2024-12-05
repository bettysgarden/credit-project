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
import feign.Request;
import feign.RequestTemplate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoanOfferServiceTest {
    @InjectMocks
    private LoanOfferServiceImpl loanOfferService;
    @Mock
    private ConveyorFeignClient conveyorFeignClient;
    @Mock
    private LoanOfferMapper loanOfferMapper;

    @Test
    void getLoanOffers_Success() {
        LoanApplicationRequest request = new LoanApplicationRequest();
        Integer applicationId = 1;
        List<LoanOfferResponse> mockResponses = List.of(new LoanOfferResponse(), new LoanOfferResponse());

        when(conveyorFeignClient.conveyorOffersPost(request)).thenReturn(mockResponses);

        List<LoanOfferResponse> responses = loanOfferService.getLoanOffers(request, applicationId);

        assertNotNull(responses);
        assertEquals(2, responses.size());
        responses.forEach(response -> assertEquals(applicationId, response.getApplicationId()));

        verify(conveyorFeignClient).conveyorOffersPost(request);
    }

    @Test
    void getLoanOffers_InvalidLoanApplicationException() {
        LoanApplicationRequest request = new LoanApplicationRequest();

        Request fakeRequest = Request.create(Request.HttpMethod.POST, "/conveyor/offers",
                new HashMap<>(), null, new RequestTemplate());

        when(conveyorFeignClient.conveyorOffersPost(request))
                .thenThrow(new FeignException.BadRequest("Bad Request", fakeRequest, null, null));

        InvalidLoanApplicationException exception = assertThrows(
                InvalidLoanApplicationException.class,
                () -> loanOfferService.getLoanOffers(request, 1)
        );
        assertTrue(exception.getErrors().contains("Bad Request"));

        verify(conveyorFeignClient).conveyorOffersPost(request);
    }


    @Test
    void getLoanOffers_ConveyorFeignClientException() {
        LoanApplicationRequest request = new LoanApplicationRequest();
        Request fakeRequest = Request.create(Request.HttpMethod.POST, "/conveyor/offers",
                new HashMap<>(), null, new RequestTemplate());
        when(conveyorFeignClient.conveyorOffersPost(request))
                .thenThrow(new FeignException.InternalServerError("Internal Error", fakeRequest, null, null));

        ConveyorFeignClientException exception = assertThrows(
                ConveyorFeignClientException.class,
                () -> loanOfferService.getLoanOffers(request, 1)
        );
        assertEquals("Ошибка при запросе предложений через FeignClient", exception.getMessage());
        verify(conveyorFeignClient).conveyorOffersPost(request);
    }

    @Test
    void setPickedLoanOffer_Success() {
        LoanOfferRequest request = new LoanOfferRequest();
        ApplicationEntity applicationEntity = new ApplicationEntity();
        AppliedLoanOffer mockAppliedLoanOffer = new AppliedLoanOffer();

        when(loanOfferMapper.toJson(request)).thenReturn(mockAppliedLoanOffer);

        AppliedLoanOffer result = loanOfferService.setPickedLoanOffer(request, applicationEntity);

        assertEquals(mockAppliedLoanOffer, result);
        verify(loanOfferMapper).toJson(request);
    }

    @Test
    void setPickedLoanOffer_StateTransitionException() {
        LoanOfferRequest request = new LoanOfferRequest();
        ApplicationEntity applicationEntity = new ApplicationEntity();
        applicationEntity.setAppliedOffer(new AppliedLoanOffer());

        StateTransitionException exception = assertThrows(
                StateTransitionException.class,
                () -> loanOfferService.setPickedLoanOffer(request, applicationEntity)
        );
        assertTrue(exception.getMessage().contains("Некорректный выбор кредитного предложения."));
    }
}
