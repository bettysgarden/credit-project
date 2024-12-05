package com.example.deal.mapper;

import com.example.deal.model.dto.CreditResponse;
import com.example.deal.model.dto.PaymentScheduleElementResponse;
import com.example.deal.model.entity.CreditEntity;
import com.example.deal.model.entity.jsonb.PaymentScheduleElement;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CreditMapperTest {

    private final CreditMapper mapper = Mappers.getMapper(CreditMapper.class);

    private static CreditResponse getCreditResponse() {
        PaymentScheduleElementResponse scheduleElementResponse = new PaymentScheduleElementResponse(
                1,
                LocalDate.of(2024, 1, 15),
                new BigDecimal("10000.00"),
                new BigDecimal("3000.00"),
                new BigDecimal("7000.00"),
                new BigDecimal("93000.00")
        );
        return new CreditResponse(
                new BigDecimal("100000"),
                12,
                new BigDecimal("8500"),
                new BigDecimal("15.0"),
                new BigDecimal("18.0"),
                true,
                false,
                List.of(scheduleElementResponse)
        );
    }

    @Test
    void testToEntity_validCreditResponse_shouldMapCorrectly() {
        CreditResponse creditResponse = getCreditResponse();

        CreditEntity creditEntity = mapper.toEntity(creditResponse);

        assertNotNull(creditEntity);
        assertNull(creditEntity.getId());

        assertEquals(creditResponse.getAmount(), creditEntity.getAmount());
        assertEquals(creditResponse.getTerm(), creditEntity.getTerm());
        assertEquals(creditResponse.getMonthlyPayment(), creditEntity.getMonthlyPayment());
        assertEquals(creditResponse.getRate(), creditEntity.getRate());
        assertEquals(creditResponse.getPsk(), creditEntity.getPsk());
        assertEquals(creditResponse.getIsInsuranceEnabled(), creditEntity.getIsInsuranceEnabled());
        assertEquals(creditResponse.getIsSalaryClient(), creditEntity.getIsSalaryClient());

        assertNotNull(creditEntity.getPaymentSchedule());
        assertEquals(creditResponse.getPaymentSchedule().size(), creditEntity.getPaymentSchedule().size());

        PaymentScheduleElement firstElement = creditEntity.getPaymentSchedule().get(0);
        assertEquals(creditResponse.getPaymentSchedule().get(0).getNumber(), firstElement.getNumber());
        assertEquals(creditResponse.getPaymentSchedule().get(0).getDate(), firstElement.getDate());
        assertEquals(creditResponse.getPaymentSchedule().get(0).getTotalPayment(), firstElement.getTotalPayment());
        assertEquals(creditResponse.getPaymentSchedule().get(0).getInterestPayment(), firstElement.getInterestPayment());
        assertEquals(creditResponse.getPaymentSchedule().get(0).getDebtPayment(), firstElement.getDebtPayment());
        assertEquals(creditResponse.getPaymentSchedule().get(0).getRemainingDebt(), firstElement.getRemainingDebt());
    }

    @Test
    void testToEntity_nullCreditResponse_shouldReturnNull() {
        CreditEntity creditEntity = mapper.toEntity(null);

        assertNull(creditEntity, "CreditEntity should be null when CreditResponse is null");
    }
}

