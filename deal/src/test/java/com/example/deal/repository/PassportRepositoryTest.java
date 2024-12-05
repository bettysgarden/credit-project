package com.example.deal.repository;

import com.example.deal.model.entity.PassportEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest(excludeAutoConfiguration = FlywayAutoConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PassportRepositoryTest {

    @Autowired
    private PassportRepository passportRepository;

    @Test
    void testSaveAndFindById() {
        PassportEntity savedPassport = getSavedPassportEntity();
        assertNotNull(savedPassport.getId(), "ID паспорта не должен быть null после сохранения");

        Optional<PassportEntity> foundPassport = passportRepository.findById(Long.valueOf(savedPassport.getId()));
        assertTrue(foundPassport.isPresent(), "Паспорт должен быть найден");
        assertEquals("1234", foundPassport.get().getSeries(), "Серия паспорта должна совпадать");
    }

    @Test
    void testUpdateEntity() {
        PassportEntity savedPassport = getSavedPassportEntity();

        savedPassport.setNumber("876543");
        savedPassport.setIssueBranch("NewBranch");
        PassportEntity updatedPassport = passportRepository.save(savedPassport);

        assertEquals("876543", updatedPassport.getNumber(), "Номер паспорта должен быть обновлен");
        assertEquals("NewBranch", updatedPassport.getIssueBranch(), "Отделение должно быть обновлено");
    }

    @Test
    void testDeleteEntity() {
        PassportEntity savedPassport = getSavedPassportEntity();

        Integer passportId = savedPassport.getId();
        passportRepository.deleteById(Long.valueOf(passportId));

        Optional<PassportEntity> deletedPassport = passportRepository.findById(Long.valueOf(passportId));
        assertFalse(deletedPassport.isPresent(), "Паспорт должен быть удален");
    }

    private PassportEntity getSavedPassportEntity() {
        PassportEntity passport = new PassportEntity();
        passport.setSeries("1234");
        passport.setNumber("567890");
        passport.setIssueBranch("TestBranch");
        passport.setIssueDate(Date.valueOf(LocalDate.of(2020, 1, 1)));
        return passportRepository.save(passport);
    }
}

