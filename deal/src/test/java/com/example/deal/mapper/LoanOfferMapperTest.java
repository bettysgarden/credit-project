package com.example.deal.mapper;

import com.example.deal.model.dto.LoanOfferRequest;
import com.example.deal.model.entity.jsonb.AppliedLoanOffer;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoanOfferMapperTest {
    private final LoanOfferMapper mapper = Mappers.getMapper(LoanOfferMapper.class);

    @Test
    void shouldMapLoanOfferRequestToAppliedLoanOffer() {
        LoanOfferRequest loanOfferRequest = new LoanOfferRequest(
                123L,
                new BigDecimal("50000.00"),
                new BigDecimal("60000.00"),
                12,
                new BigDecimal("5000.00"),
                new BigDecimal("9.5"),
                true,
                false
        );

        AppliedLoanOffer appliedLoanOffer = mapper.toJson(loanOfferRequest);

        assertThat(appliedLoanOffer).isNotNull();
        assertEquals(appliedLoanOffer.getApplicationId(), loanOfferRequest.getApplicationId());
        assertEquals(appliedLoanOffer.getRequestedAmount(), loanOfferRequest.getRequestedAmount());
        assertEquals(appliedLoanOffer.getTotalAmount(), loanOfferRequest.getTotalAmount());
        assertEquals(appliedLoanOffer.getTerm(), loanOfferRequest.getTerm());
        assertEquals(appliedLoanOffer.getMonthlyPayment(), loanOfferRequest.getMonthlyPayment());
        assertEquals(appliedLoanOffer.getRate(), loanOfferRequest.getRate());
        assertEquals(appliedLoanOffer.getIsInsuranceEnabled(), loanOfferRequest.getIsInsuranceEnabled());
        assertEquals(appliedLoanOffer.getIsSalaryClient(), loanOfferRequest.getIsSalaryClient());
    }
}
