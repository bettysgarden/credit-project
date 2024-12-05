package com.example.deal.repository;

import com.example.deal.model.entity.CreditEntity;
import com.example.deal.model.entity.CreditStatusEntity;
import com.example.deal.model.entity.jsonb.PaymentScheduleElement;
import com.example.deal.model.enums.CreditStatusEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest(excludeAutoConfiguration = FlywayAutoConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CreditRepositoryTest {
    @Autowired
    private CreditRepository creditRepository;

    @Test
    void testSaveAndFindById() {
        CreditEntity savedCredit = getSavedCreditEntity();
        assertNotNull(savedCredit.getId());

        Optional<CreditEntity> foundCredit = creditRepository.findById(Long.valueOf(savedCredit.getId()));
        assertTrue(foundCredit.isPresent(), "Объект должен быть найден");
        assertEquals(new BigDecimal("500000"), foundCredit.get().getAmount(), "Сумма должна совпадать");
    }

    @Test
    void testUpdateEntity() {
        CreditEntity savedCredit = getSavedCreditEntity();

        savedCredit.setAmount(new BigDecimal("350000"));
        savedCredit.setTerm(18);
        CreditEntity updatedCredit = creditRepository.save(savedCredit);

        Optional<CreditEntity> foundCredit = creditRepository.findById(Long.valueOf(updatedCredit.getId()));
        assertTrue(foundCredit.isPresent(), "Объект должен быть найден");
        assertEquals(new BigDecimal("350000"), foundCredit.get().getAmount(), "Сумма должна обновиться");
        assertEquals(18, foundCredit.get().getTerm(), "Срок должен обновиться");
    }

    @Test
    void testDeleteEntity() {
        CreditEntity savedCredit = getSavedCreditEntity();

        creditRepository.deleteById(Long.valueOf(savedCredit.getId()));

        Optional<CreditEntity> foundCredit = creditRepository.findById(Long.valueOf(savedCredit.getId()));
        assertFalse(foundCredit.isPresent(), "Объект должен быть удален");
    }

    private CreditEntity getSavedCreditEntity() {
        CreditEntity credit = new CreditEntity();

        credit.setAmount(new BigDecimal("500000"));
        credit.setTerm(24);
        credit.setMonthlyPayment(new BigDecimal("23000"));
        credit.setRate(new BigDecimal("10.5"));
        credit.setPsk(new BigDecimal("12.0"));
        credit.setIsInsuranceEnabled(true);
        credit.setIsSalaryClient(false);
        credit.setPaymentSchedule(List.of(new PaymentScheduleElement(1, LocalDate.now(), new BigDecimal("23000"), new BigDecimal("5000"), new BigDecimal("18000"), new BigDecimal("18000"))));
        credit.setStatus(new CreditStatusEntity(1, CreditStatusEnum.ISSUED));

        return creditRepository.save(credit);
    }
}
