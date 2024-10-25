package ru.test.conveyor.loan;

import com.example.credit.application.model.LoanApplicationRequestDTO;
import com.example.credit.application.model.LoanOfferDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.test.conveyor.exception.InvalidLoanApplicationException;
import ru.test.conveyor.mapper.LoanApplicationMapper;
import ru.test.conveyor.mapper.LoanOfferMapper;
import ru.test.conveyor.model.entity.LoanApplication;
import ru.test.conveyor.model.entity.LoanOffer;
import ru.test.conveyor.service.LoanService;
import ru.test.conveyor.service.LoanServiceImpl;
import ru.test.conveyor.util.LoanApplicationValidator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
class LoanServiceImplTest {

    @InjectMocks
    private LoanServiceImpl loanService;

    @Spy
    private LoanOfferMapper loanOfferMapper = LoanOfferMapper.INSTANCE;

    @Spy
    private LoanApplicationMapper loanApplicationMapper = LoanApplicationMapper.INSTANCE;

    @Mock
    private LoanApplicationValidator validator;

    private LoanApplicationRequestDTO validLoanApplicationDTO;

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

        LoanOffer loanOffer = new LoanOffer();
        loanOffer.setTotalAmount(BigDecimal.valueOf(50000));
        loanOffer.setRate(BigDecimal.valueOf(12));
        loanOffer.setTerm(12);
        loanOffer.setMonthlyPayment(BigDecimal.valueOf(4500));
    }

    @Test
    public void testGetLoanOffers_Success() {
        LoanService loanServiceSpy = spy(loanService);

        List<LoanOfferDTO> offers = loanServiceSpy.getLoanOffers(validLoanApplicationDTO);

        assertNotNull(offers);
        assertEquals(4, offers.size());

        LoanOfferDTO firstOffer = offers.get(0);
        assertNotNull(firstOffer);
        assertEquals(12, firstOffer.getTerm());

        LoanOfferDTO secondOffer = offers.get(1);
        assertNotNull(secondOffer);
    }

    @Test
    void testGetLoanOffers_InvalidApplication() {
        validLoanApplicationDTO.setAmount(null);

        assertThrows(
                InvalidLoanApplicationException.class,
                () -> loanService.getLoanOffers(validLoanApplicationDTO)
        );

    }

    @Test
    void testGetLoanOffer_InsuranceAndSalaryClient() {
        LoanApplication application = new LoanApplication();
        application.setAmount(BigDecimal.valueOf(100000));
        application.setTerm(12);

        LoanOffer offer = loanService.getLoanOffer(application, true, true);

        assertEquals(new BigDecimal("19"), offer.getRate());
    }

    @Test
    void testGetLoanOffer_OnlyInsurance() {
        LoanApplication application = new LoanApplication();
        application.setAmount(BigDecimal.valueOf(100000));
        application.setTerm(12);

        LoanOffer offer = loanService.getLoanOffer(application, true, false);

        assertEquals(new BigDecimal("20"), offer.getRate());
    }

    @Test
    void testGetLoanOffer_OnlySalaryClient() {
        LoanApplication application = new LoanApplication();
        application.setAmount(BigDecimal.valueOf(100000));
        application.setTerm(12);

        LoanOffer offer = loanService.getLoanOffer(application, false, true);

        assertEquals(new BigDecimal("24"), offer.getRate());
    }

    @Test
    void testGetLoanOffer_NoInsuranceNoSalaryClient() {
        LoanApplication application = new LoanApplication();
        application.setAmount(BigDecimal.valueOf(100000));
        application.setTerm(12);

        LoanOffer offer = loanService.getLoanOffer(application, false, false);

        assertEquals(new BigDecimal("25"), offer.getRate());
    }

}
