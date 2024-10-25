package ru.test.conveyor.mapper;

import com.example.credit.application.model.LoanOfferDTO;

import org.junit.jupiter.api.Test;
import ru.test.conveyor.model.entity.LoanOffer;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LoanOfferMapperTest {
    private final LoanOfferMapper mapper = LoanOfferMapper.INSTANCE;

    @Test
    void toDTO_shouldMapLoanOfferToLoanOfferDTO() {
        LoanOffer loanOffer = new LoanOffer();
        loanOffer.setApplicationId(1L);
        loanOffer.setRequestedAmount(new BigDecimal("100000"));
        loanOffer.setTotalAmount(new BigDecimal("120000"));
        loanOffer.setTerm(36);
        loanOffer.setMonthlyPayment(new BigDecimal("3500"));
        loanOffer.setRate(new BigDecimal("6.5"));
        loanOffer.setIsInsuranceEnabled(true);
        loanOffer.setIsSalaryClient(false);

        LoanOfferDTO loanOfferDTO = mapper.toDTO(loanOffer);

        assertNotNull(loanOfferDTO);
        assertEquals(loanOffer.getApplicationId(), loanOfferDTO.getApplicationId());
        assertEquals(loanOffer.getRequestedAmount(), loanOfferDTO.getRequestedAmount());
        assertEquals(loanOffer.getTotalAmount(), loanOfferDTO.getTotalAmount());
        assertEquals(loanOffer.getTerm(), loanOfferDTO.getTerm());
        assertEquals(loanOffer.getMonthlyPayment(), loanOfferDTO.getMonthlyPayment());
        assertEquals(loanOffer.getRate(), loanOfferDTO.getRate());
        assertEquals(loanOffer.getIsInsuranceEnabled(), loanOfferDTO.getIsInsuranceEnabled());
        assertEquals(loanOffer.getIsSalaryClient(), loanOfferDTO.getIsSalaryClient());
    }
}
