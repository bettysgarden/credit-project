package ru.test.conveyor;

import org.junit.jupiter.api.Test;
import ru.test.conveyor.model.entity.LoanApplication;
import ru.test.conveyor.model.entity.PaymentScheduleElement;
import ru.test.conveyor.util.CreditCalculator;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreditCalculatorTest {

    @Test
    void testCalculateInsuranceCost() {
        LoanApplication application = new LoanApplication();
        application.setAmount(BigDecimal.valueOf(200000));
        application.setTerm(24);

        BigDecimal insuranceCost = CreditCalculator.calculateInsuranceCost(application);
        BigDecimal expectedCost = new BigDecimal("14800.0000");

        assertEquals(expectedCost, insuranceCost, "Стоимость страховки рассчитана неверно.");
    }

    @Test
    void testCalculateMonthlyPayment() {
        BigDecimal amount = BigDecimal.valueOf(200000);
        BigDecimal rate = BigDecimal.valueOf(12);
        Integer term = 24;

        BigDecimal monthlyPayment = CreditCalculator.calculateMonthlyPayment(amount, rate, term);
        BigDecimal expectedMonthlyPayment = new BigDecimal("9414.69");

        assertEquals(expectedMonthlyPayment, monthlyPayment, "Ежемесячный платеж рассчитан неверно.");
    }

    @Test
    void testCalculatePsk() {
        BigDecimal monthlyPayment = BigDecimal.valueOf(9416.88);
        Integer term = 24;

        BigDecimal psk = CreditCalculator.calculatePsk(monthlyPayment, term);
        BigDecimal expectedPsk = new BigDecimal("226005.12");

        assertEquals(expectedPsk, psk, "Полная стоимость кредита (ПСК) рассчитана неверно.");
    }

    @Test
    void testCalculatePaymentSchedule() {
        BigDecimal amount = BigDecimal.valueOf(200000);
        BigDecimal rate = BigDecimal.valueOf(12);
        Integer term = 24;
        BigDecimal monthlyPayment = new BigDecimal("9416.88");

        ArrayList<PaymentScheduleElement> paymentSchedule = CreditCalculator.calculatePaymentSchedule(monthlyPayment, term, amount, rate);

        assertEquals(term, paymentSchedule.size(), "Размер платежного графика должен быть равен количеству месяцев (срок кредита).");

        PaymentScheduleElement firstElement = paymentSchedule.get(0);
        BigDecimal expectedFirstInterestPayment = new BigDecimal("2000.00");
        BigDecimal expectedFirstDebtPayment = new BigDecimal("7416.88");
        BigDecimal expectedRemainingDebtAfterFirstPayment = new BigDecimal("192583.12");

        assertEquals(expectedFirstInterestPayment, firstElement.getInterestPayment(), "Первый платеж процентов рассчитан неверно.");
        assertEquals(expectedFirstDebtPayment, firstElement.getDebtPayment(), "Первый платеж по долгу рассчитан неверно.");
        assertEquals(expectedRemainingDebtAfterFirstPayment, firstElement.getRemainingDebt(), "Остаток долга после первого платежа рассчитан неверно.");
    }
}
