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
import ru.test.conveyor.mapper.LoanApplicationMapper;
import ru.test.conveyor.mapper.LoanOfferMapper;
import ru.test.conveyor.service.LoanServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        loanService = new LoanServiceImpl(loanOfferMapper, loanApplicationMapper);
        LoanServiceImpl loanServiceSpy = spy(loanService);

        doReturn(true).when(loanServiceSpy).prescoring(validLoanApplication);

        List<LoanOfferDTO> offers = loanServiceSpy.getLoanOffers(validLoanApplicationDTO);

        assertNotNull(offers, "Список предложений не должен быть null");
        assertEquals(4, offers.size(), "Должно быть сгенерировано 4 предложения");

        // Проверяем свойства первого предложения, например
        LoanOfferDTO firstOffer = offers.get(0);
        assertNotNull(firstOffer, "Первое предложение не должно быть null");
        assertEquals(12, firstOffer.getTerm(), "Срок предложения должен быть 12 месяцев");

        LoanOfferDTO secondOffer = offers.get(1);
        assertNotNull(secondOffer, "Второе предложение не должно быть null");

        doReturn(false).when(loanServiceSpy).prescoring(validLoanApplication);
        List<LoanOfferDTO> emptyOffers = loanServiceSpy.getLoanOffers(validLoanApplicationDTO);
        assertTrue(emptyOffers.isEmpty(), "Если прескоринг не пройден, предложения не должны генерироваться");
    }


    @Test
    public void testGetLoanOffers_PrescoringFailed() {
        loanService = new LoanServiceImpl(loanOfferMapper, loanApplicationMapper);
        LoanServiceImpl loanServiceSpy = spy(loanService);
        doReturn(false).when(loanServiceSpy).prescoring(any(LoanApplication.class));

        List<LoanOfferDTO> offers = loanServiceSpy.getLoanOffers(validLoanApplicationDTO);

        assertNotNull(offers);
        assertEquals(0, offers.size(), "Предложения не должны генерироваться, если прескоринг не пройден");
    }

    @Test
    public void testPrescoring_ValidData() {
        boolean result = loanService.prescoring(validLoanApplication);
        assertTrue(result, "Прескоринг должен быть успешным для валидации данных");
    }

    @Test
    public void testPrescoring_InvalidName() {
        validLoanApplication.setFirstName("J1");
        boolean result = loanService.prescoring(validLoanApplication);
        assertFalse(result, "Прескоринг должен быть неуспешным при некорректном имени");
    }

    @Test
    public void testPrescoring_InvalidAmount() {
        validLoanApplication.setAmount(BigDecimal.valueOf(1));
        boolean result = loanService.prescoring(validLoanApplication);
        assertFalse(result, "Прескоринг должен быть неуспешным при сумме кредита меньше минимальной");
    }

    @Test
    public void testPrescoring_InvalidAge() {
        validLoanApplication.setBirthdate(LocalDate.now().minusYears(17));
        boolean result = loanService.prescoring(validLoanApplication);
        assertFalse(result, "Прескоринг должен быть неуспешным при возрасте младше 18 лет");
    }
}
