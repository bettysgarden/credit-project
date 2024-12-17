package ru.test.conveyor.loan;

import com.example.credit.application.model.LoanApplicationRequestDTO;
import com.example.credit.application.model.LoanOfferDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.test.conveyor.exception.LoanCalculationException;
import ru.test.conveyor.mapper.LoanApplicationMapper;
import ru.test.conveyor.mapper.LoanOfferMapper;
import ru.test.conveyor.model.entity.LoanApplication;
import ru.test.conveyor.model.entity.LoanOffer;
import ru.test.conveyor.service.LoanServiceImpl;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceImplTest {

    @InjectMocks
    private LoanServiceImpl loanService;

    @Mock
    private LoanOfferMapper loanOfferMapper;

    @Mock
    private LoanApplicationMapper loanApplicationMapper;

    private LoanApplicationRequestDTO validLoanApplicationDTO;
    private LoanOfferDTO loanOfferDTO;

    @BeforeEach
    public void setUp() {
        LoanApplication validLoanApplication = new LoanApplication();
        validLoanApplication.setAmount(new BigDecimal("100000"));
        validLoanApplication.setTerm(12);

        loanOfferDTO = new LoanOfferDTO();
        validLoanApplicationDTO = new LoanApplicationRequestDTO();

        when(loanApplicationMapper.toEntity(any(LoanApplicationRequestDTO.class))).thenReturn(validLoanApplication);
    }

    @Test
    public void testGetLoanOffers_Success() {
        when(loanOfferMapper.toDTO(any(LoanOffer.class))).thenReturn(loanOfferDTO);

        List<LoanOfferDTO> offers = loanService.getLoanOffers(validLoanApplicationDTO);

        assertThat(offers).isNotNull();
        assertThat(offers.size()).isEqualTo(4);
        verify(loanApplicationMapper).toEntity(validLoanApplicationDTO);
        verify(loanOfferMapper, times(4)).toDTO(any(LoanOffer.class));
    }

    @Test
    void getLoanOffers_UnexpectedException_ThrowsLoanCalculationException() {
        doThrow(new RuntimeException("Unexpected error")).when(loanOfferMapper).toDTO(any(LoanOffer.class));

        LoanCalculationException exception = assertThrows(
                LoanCalculationException.class,
                () -> loanService.getLoanOffers(validLoanApplicationDTO)
        );

        assertEquals("Ошибка при расчете предложений.", exception.getMessage());
        verify(loanApplicationMapper).toEntity(validLoanApplicationDTO);
        verify(loanOfferMapper).toDTO(any(LoanOffer.class));
    }

}
