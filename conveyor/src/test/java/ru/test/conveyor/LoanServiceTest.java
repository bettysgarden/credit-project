package ru.test.conveyor;

import com.example.credit.application.model.LoanApplicationRequestDTO;
import com.example.credit.application.model.LoanOfferDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.test.conveyor.entity.LoanApplication;
import ru.test.conveyor.entity.LoanOffer;
import ru.test.conveyor.mapper.LoanApplicationMapper;
import ru.test.conveyor.mapper.LoanOfferMapper;
import ru.test.conveyor.service.LoanServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class LoanServiceTest {

    @InjectMocks
    private LoanServiceImpl loanService;

    @Mock
    private LoanOfferMapper loanOfferMapper;

    @Mock
    private LoanApplicationMapper loanApplicationMapper;

    private LoanApplicationRequestDTO validLoanApplicationDTO;
    private LoanApplication validLoanApplication;
    private LoanOffer loanOffer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        validLoanApplicationDTO = new LoanApplicationRequestDTO();
        validLoanApplicationDTO.setFirstName("John");
        validLoanApplicationDTO.setLastName("Doe");
        validLoanApplicationDTO.setMiddleName("Middle");
        validLoanApplicationDTO.setBirthdate(LocalDate.of(1985, 1, 1));
        validLoanApplicationDTO.setAmount(BigDecimal.valueOf(50000));
        validLoanApplicationDTO.setTerm(12);
        validLoanApplicationDTO.setEmail("john.doe@example.com");
        validLoanApplicationDTO.setPassportSeries("1234");
        validLoanApplicationDTO.setPassportNumber("567890");

        validLoanApplication = new LoanApplication();
        validLoanApplication.setFirstName("John");
        validLoanApplication.setLastName("Doe");
        validLoanApplication.setMiddleName("Middle");
        validLoanApplication.setBirthdate(LocalDate.of(1985, 1, 1));
        validLoanApplication.setAmount(BigDecimal.valueOf(50000));
        validLoanApplication.setTerm(12);
        validLoanApplication.setEmail("john.doe@example.com");
        validLoanApplication.setPassportSeries("1234");
        validLoanApplication.setPassportNumber("567890");

        loanOffer = new LoanOffer();
        loanOffer.setTotalAmount(BigDecimal.valueOf(50000));
        loanOffer.setRate(BigDecimal.valueOf(12));
        loanOffer.setTerm(12);
        loanOffer.setMonthlyPayment(BigDecimal.valueOf(4500));

        when(loanApplicationMapper.toEntity(any(LoanApplicationRequestDTO.class))).thenReturn(validLoanApplication);
        when(loanOfferMapper.toDTO(any(LoanOffer.class))).thenReturn(new LoanOfferDTO());
    }


    @Test
    public void testGetLoanOffers_PrescoringFailed() {
        LoanServiceImpl loanServiceSpy = spy(loanService);
        doReturn(false).when(loanServiceSpy).prescoring(any(LoanApplication.class));

        List<LoanOfferDTO> offers = loanServiceSpy.getLoanOffers(validLoanApplicationDTO);

        assertNotNull(offers);
        assertEquals(0, offers.size(), "Предложения не должны генерироваться, если prescoring не пройден");

        verify(loanApplicationMapper).toEntity(validLoanApplicationDTO);

        verify(loanOfferMapper, never()).toDTO(any());
    }

    @Disabled
    @Test
    public void testGetLoanOffers_Success() {
        LoanServiceImpl loanServiceSpy = spy(loanService);
        doReturn(true).when(loanServiceSpy).prescoring(validLoanApplication);

        List<LoanOfferDTO> offers = loanService.getLoanOffers(validLoanApplicationDTO);

        assertNotNull(offers);
        assertEquals(4, offers.size(), "Должно быть сгенерировано 4 предложения");

        verify(loanApplicationMapper).toEntity(validLoanApplicationDTO);
        verify(loanOfferMapper, times(4)).toDTO(any(LoanOffer.class));

    }

    @Test
    public void testPrescoring_ValidData() {
        boolean result = loanService.prescoring(validLoanApplication);
        assertTrue(result, "Prescoring должен быть успешным для валидации данных");
    }

    @Test
    public void testPrescoring_InvalidName() {
        validLoanApplication.setFirstName("J1");
        boolean result = loanService.prescoring(validLoanApplication);
        assertFalse(result, "Prescoring должен быть неуспешным при некорректном имени");
    }

    @Test
    public void testPrescoring_InvalidAmount() {
        validLoanApplication.setAmount(BigDecimal.valueOf(1));
        boolean result = loanService.prescoring(validLoanApplication);
        assertFalse(result, "Prescoring должен быть неуспешным при сумме кредита меньше минимальной");
    }

    @Test
    public void testPrescoring_InvalidAge() {
        validLoanApplication.setBirthdate(LocalDate.now().minusYears(17));
        boolean result = loanService.prescoring(validLoanApplication);
        assertFalse(result, "Prescoring должен быть неуспешным при возрасте младше 18 лет");
    }
}
