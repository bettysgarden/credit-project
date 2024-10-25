package ru.test.conveyor.util;

import lombok.extern.slf4j.Slf4j;
import ru.test.conveyor.model.entity.LoanApplication;
import ru.test.conveyor.model.entity.PaymentScheduleElement;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;

@Slf4j
public class CreditCalculator {

    public static BigDecimal calculateInsuranceCost(LoanApplication application) {
        // 10000 + (запрашиваемая_сумма/1000) * (количество_платежных_периодов)
        BigDecimal insuranceBase = new BigDecimal("10000");
        BigDecimal insuranceVariable = application.getAmount().divide(BigDecimal.valueOf(1000), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(application.getTerm()));
        BigDecimal totalInsuranceCost = insuranceBase.add(insuranceVariable);
        log.info("Расчет стоимости страховки: базовая часть {}, переменная часть {}, итоговая сумма {}", insuranceBase, insuranceVariable, totalInsuranceCost);
        return totalInsuranceCost;
    }

    public static BigDecimal calculateMonthlyPayment(BigDecimal amount, BigDecimal rate, Integer term) {
        BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP).divide(BigDecimal.valueOf(12), 6, RoundingMode.HALF_UP);
        BigDecimal monthlyPayment = amount.
                multiply(monthlyRate).divide(
                        BigDecimal.ONE.subtract(
                                BigDecimal.ONE.divide(
                                        (BigDecimal.ONE.add(monthlyRate)).pow(term), 6, RoundingMode.HALF_UP)),
                        2, RoundingMode.HALF_UP);
        log.info("Расчет аннуитетного платежа: сумма {}, ставка {}, срок {}, ежемесячный платеж {}", amount, rate, term, monthlyPayment);
        return monthlyPayment;
    }

    public static BigDecimal calculatePsk(BigDecimal monthlyPayment, Integer term) {
        return monthlyPayment.multiply(BigDecimal.valueOf(term));
    }

    public static ArrayList<PaymentScheduleElement> calculatePaymentSchedule(BigDecimal monthlyPayment, Integer term, BigDecimal amount, BigDecimal rate) {
        ArrayList<PaymentScheduleElement> paymentSchedule = new ArrayList<>();
        BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP).divide(BigDecimal.valueOf(12), 6, RoundingMode.HALF_UP);

        for (int i = 1; i <= term; i++) {
            PaymentScheduleElement paymentScheduleElement = new PaymentScheduleElement();
            paymentScheduleElement.setNumber(i);
            paymentScheduleElement.setDate(LocalDate.now().plusMonths(i));
            paymentScheduleElement.setTotalPayment(monthlyPayment);

            BigDecimal interest = amount.multiply(monthlyRate).divide(BigDecimal.valueOf(1), 2, RoundingMode.HALF_UP);
            paymentScheduleElement.setInterestPayment(interest);

            BigDecimal monthlyDebtPayment = monthlyPayment.subtract(interest);
            paymentScheduleElement.setDebtPayment(monthlyDebtPayment);

            amount = amount.subtract(monthlyDebtPayment);
            paymentScheduleElement.setRemainingDebt(amount);

            paymentSchedule.add(paymentScheduleElement);
        }

        return paymentSchedule;
    }
}
