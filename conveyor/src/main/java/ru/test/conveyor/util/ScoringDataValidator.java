package ru.test.conveyor.util;

import org.springframework.stereotype.Component;
import ru.test.conveyor.model.entity.Employment;
import ru.test.conveyor.model.entity.ScoringData;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Component
public class ScoringDataValidator {

    public List<String> validateScoringData(ScoringData scoringData) {
        List<String> errors = new ArrayList<>();

        if (scoringData.getBirthdate() == null) {
            errors.add("Дата рождения не указана.");
        } else {
            int age = Period.between(scoringData.getBirthdate(), LocalDate.now()).getYears();
            if (age < 20 || age > 60) {
                errors.add("Клиент не прошёл по возрасту.");
            }
        }

        Employment employment = scoringData.getEmployment();
        if (employment == null) {
            errors.add("Данные о занятости не указаны.");
        } else {
            if (employment.getWorkExperienceTotal() == null || employment.getWorkExperienceCurrent() == null) {
                errors.add("Недостаточные данные о стаже работы.");
            } else if (employment.getWorkExperienceTotal() < 12 || employment.getWorkExperienceCurrent() < 3) {
                errors.add("Недостаточный стаж работы.");
            }

            if (scoringData.getAmount() == null || employment.getSalary() == null) {
                errors.add("Не указаны сумма кредита или зарплата.");
            } else if (scoringData.getAmount().compareTo(employment.getSalary().multiply(BigDecimal.valueOf(20))) > 0) {
                errors.add("Сумма кредита превышает допустимое соотношение 20-кратного размера зарплаты.");
            }
        }

        if (scoringData.getTerm() == null || scoringData.getTerm() < 6 || scoringData.getTerm() > 60) {
            errors.add("Неверно указан срок кредита.");
        }

        if (scoringData.getDependentAmount() != null && scoringData.getDependentAmount() < 0) {
            errors.add("Некорректное количество иждивенцев.");
        }

        if (scoringData.getIsInsuranceEnabled() == null) {
            errors.add("Не указано, оформлена ли страховка.");
        }

        if (scoringData.getIsSalaryClient() == null) {
            errors.add("Не указано, является ли клиент зарплатным клиентом.");
        }

        return errors;
    }
}
