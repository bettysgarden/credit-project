package ru.test.conveyor.mapper;

import com.example.credit.application.model.CreditDTO;
import org.junit.jupiter.api.Test;
import ru.test.conveyor.model.entity.Credit;
import ru.test.conveyor.model.entity.PaymentScheduleElement;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CreditMapperTest {
    private final CreditMapper mapper = CreditMapper.INSTANCE;

    @Test
    void toDto_shouldMapCreditToCreditDTO() {
        Credit credit = new Credit();
        credit.setAmount(new BigDecimal("100000"));
        credit.setTerm(12);
        credit.setMonthlyPayment(new BigDecimal("8500"));
        credit.setRate(new BigDecimal("5.5"));
        credit.setPsk(new BigDecimal("6.0"));
        credit.setIsInsuranceEnabled(true);
        credit.setIsSalaryClient(false);
        credit.setPaymentSchedule(List.of(new PaymentScheduleElement()));

        CreditDTO creditDTO = mapper.toDto(credit);

        assertNotNull(creditDTO);
        assertEquals(credit.getAmount(), creditDTO.getAmount());
        assertEquals(credit.getTerm(), creditDTO.getTerm());
        assertEquals(credit.getMonthlyPayment(), creditDTO.getMonthlyPayment());
        assertEquals(credit.getRate(), creditDTO.getRate());
        assertEquals(credit.getPsk(), creditDTO.getPsk());
        assertEquals(credit.getIsInsuranceEnabled(), creditDTO.getIsInsuranceEnabled());
        assertEquals(credit.getIsSalaryClient(), creditDTO.getIsSalaryClient());
        assertEquals(credit.getPaymentSchedule().size(), creditDTO.getPaymentSchedule().size());
    }
}
