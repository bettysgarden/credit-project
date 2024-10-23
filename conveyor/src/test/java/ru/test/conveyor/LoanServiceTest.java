package ru.test.conveyor;

import com.example.credit.application.model.LoanApplicationRequestDTO;
import com.example.credit.application.model.LoanOfferDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.test.conveyor.entity.LoanApplication;
import ru.test.conveyor.entity.LoanOffer;
import ru.test.conveyor.exception.InvalidLoanApplicationException;
import ru.test.conveyor.mapper.LoanApplicationMapper;
import ru.test.conveyor.mapper.LoanOfferMapper;
import ru.test.conveyor.service.LoanServiceImpl;
import ru.test.conveyor.util.LoanApplicationValidator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@SpringBootTest
public class LoanServiceTest {

    @InjectMocks
    private LoanServiceImpl loanService;

    @Mock
    private LoanOfferMapper loanOfferMapperMock;

    @Mock
    private LoanApplicationMapper loanApplicationMapperMock;

    @Autowired
    private LoanOfferMapper loanOfferMapper;
    @Autowired
    private LoanApplicationMapper loanApplicationMapper;

    private LoanApplicationRequestDTO validLoanApplicationDTO;
    private LoanApplication validLoanApplication;
    private LoanApplicationValidator validator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        loanService = new LoanServiceImpl(loanOfferMapper, loanApplicationMapper);
        validator = new LoanApplicationValidator();

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

        LoanOffer loanOffer = new LoanOffer();
        loanOffer.setTotalAmount(BigDecimal.valueOf(50000));
        loanOffer.setRate(BigDecimal.valueOf(12));
        loanOffer.setTerm(12);
        loanOffer.setMonthlyPayment(BigDecimal.valueOf(4500));

        when(loanApplicationMapperMock.toEntity(any(LoanApplicationRequestDTO.class))).thenReturn(validLoanApplication);
        when(loanOfferMapperMock.toDTO(any(LoanOffer.class))).thenReturn(new LoanOfferDTO());
    }

    @Test
    public void testGetLoanOffers_Success() {
        LoanServiceImpl loanServiceSpy = spy(loanService);

        List<String> validationErrors = validator.validate(validLoanApplication);
        assertTrue(validationErrors.isEmpty());

        List<LoanOfferDTO> offers = loanServiceSpy.getLoanOffers(validLoanApplicationDTO);

        assertNotNull(offers, "Список предложений не должен быть null");
        assertEquals(4, offers.size(), "Должно быть сгенерировано 4 предложения");

        LoanOfferDTO firstOffer = offers.get(0);
        assertNotNull(firstOffer, "Первое предложение не должно быть null");
        assertEquals(12, firstOffer.getTerm(), "Срок предложения должен быть 12 месяцев");

        LoanOfferDTO secondOffer = offers.get(1);
        assertNotNull(secondOffer, "Второе предложение не должно быть null");

    }

    @Test
    public void testGetLoanOffers_PrescoringFailed() {
        LoanServiceImpl loanServiceSpy = spy(loanService);
        validLoanApplication.setFirstName("11111");
        List<String> validationErrors = validator.validate(validLoanApplication);
        assertFalse(validationErrors.isEmpty());

        validLoanApplicationDTO.setFirstName("11111");
        assertThrows(InvalidLoanApplicationException.class, () -> loanServiceSpy.getLoanOffers(validLoanApplicationDTO));
    }

    @Test
    public void testPrescoring_ValidData() {
        List<String> validationErrors = validator.validate(validLoanApplication);
        assertTrue(validationErrors.isEmpty());
    }

    @Test
    public void testPrescoring_InvalidName() {
        validLoanApplication.setLastName("J1");
        List<String> validationErrors = validator.validate(validLoanApplication);

        assertTrue(validationErrors.contains("Invalid last name"));
    }

    @Test
    public void testPrescoring_InvalidAmount() {
        validLoanApplication.setAmount(BigDecimal.valueOf(1));
        List<String> validationErrors = validator.validate(validLoanApplication);

        assertTrue(validationErrors.contains("Loan amount is below the minimum allowed value"));
    }

    @Test
    public void testPrescoring_InvalidAge() {
        validLoanApplication.setBirthdate(LocalDate.now().minusYears(17));
        List<String> validationErrors = validator.validate(validLoanApplication);

        assertTrue(validationErrors.contains("Applicant is not of legal age"));
    }
}
