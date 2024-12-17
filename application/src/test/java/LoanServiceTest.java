import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.test.application.exception.DealFeignClientException;
import ru.test.application.exception.InvalidLoanApplicationException;
import ru.test.application.feign.DealFeignClient;
import ru.test.application.mapper.LoanApplicationMapper;
import ru.test.application.model.dto.LoanApplicationRequest;
import ru.test.application.model.dto.LoanOfferRequest;
import ru.test.application.model.dto.LoanOfferResponse;
import ru.test.application.model.entity.LoanApplication;
import ru.test.application.service.impl.LoanServiceImpl;
import ru.test.application.util.LoanApplicationValidator;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoanServiceTest {

    @InjectMocks
    private LoanServiceImpl loanService;

    @Mock
    private LoanApplicationValidator validator;

    @Mock
    private DealFeignClient dealFeignClient;

    @Mock
    private LoanApplicationMapper loanApplicationMapper;

    @Test
    void testGetLoanOffers_Success() {
        LoanApplicationRequest loanApplicationRequest = new LoanApplicationRequest();
        LoanApplication loanApplication = new LoanApplication();
        LoanOfferResponse loanOfferResponse = new LoanOfferResponse();

        when(loanApplicationMapper.toEntity(loanApplicationRequest)).thenReturn(loanApplication);
        when(validator.validate(loanApplication)).thenReturn(Collections.emptyList());
        when(dealFeignClient.dealCreateApplicationPost(loanApplicationRequest))
                .thenReturn(List.of(loanOfferResponse));

        List<LoanOfferResponse> result = loanService.getLoanOffers(loanApplicationRequest);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(loanOfferResponse, result.get(0));
        verify(validator).validate(loanApplication);
        verify(dealFeignClient).dealCreateApplicationPost(loanApplicationRequest);
    }

    @Test
    void testGetLoanOffers_InvalidLoanApplicationException() {
        LoanApplicationRequest loanApplicationRequest = new LoanApplicationRequest();
        LoanApplication loanApplication = new LoanApplication();

        when(loanApplicationMapper.toEntity(loanApplicationRequest)).thenReturn(loanApplication);
        when(validator.validate(loanApplication)).thenReturn(List.of("Error 1"));

        InvalidLoanApplicationException exception = assertThrows(
                InvalidLoanApplicationException.class,
                () -> loanService.getLoanOffers(loanApplicationRequest)
        );

        assertNotNull(exception.getErrors());
        assertTrue(exception.getErrors().contains("Error 1"));
        verify(validator).validate(loanApplication);
        verifyNoInteractions(dealFeignClient);
    }

    @Test
    void testGetLoanOffers_DealFeignClientException() {
        LoanApplicationRequest loanApplicationRequest = new LoanApplicationRequest();
        LoanApplication loanApplication = new LoanApplication();

        when(loanApplicationMapper.toEntity(loanApplicationRequest)).thenReturn(loanApplication);
        when(validator.validate(loanApplication)).thenReturn(Collections.emptyList());
        when(dealFeignClient.dealCreateApplicationPost(loanApplicationRequest))
                .thenThrow(mock(FeignException.class));

        DealFeignClientException exception = assertThrows(
                DealFeignClientException.class,
                () -> loanService.getLoanOffers(loanApplicationRequest)
        );

        assertEquals("Ошибка при запросе предложений через FeignClient.msa-deal", exception.getMessage());
        verify(validator).validate(loanApplication);
        verify(dealFeignClient).dealCreateApplicationPost(loanApplicationRequest);
    }

    @Test
    void testUpdateLoanOfferForApplication_Success() {
        LoanOfferRequest loanOfferRequest = new LoanOfferRequest();

        loanService.updateLoanOfferForApplication(loanOfferRequest);

        verify(dealFeignClient).dealSetOfferApplicationPut(loanOfferRequest);
    }

    @Test
    void testUpdateLoanOfferForApplication_DealFeignClientException() {
        LoanOfferRequest loanOfferRequest = new LoanOfferRequest();

        doThrow(mock(FeignException.class))
                .when(dealFeignClient).dealSetOfferApplicationPut(loanOfferRequest);

        DealFeignClientException exception = assertThrows(
                DealFeignClientException.class,
                () -> loanService.updateLoanOfferForApplication(loanOfferRequest)
        );

        assertEquals("Ошибка при обновлении предложения через FeignClient", exception.getMessage());
        verify(dealFeignClient).dealSetOfferApplicationPut(loanOfferRequest);
    }
}
