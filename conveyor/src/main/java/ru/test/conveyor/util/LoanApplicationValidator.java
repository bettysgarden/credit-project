package ru.test.conveyor.util;

import org.springframework.stereotype.Component;
import ru.test.conveyor.model.entity.LoanApplication;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class LoanApplicationValidator {

    public List<String> validate(LoanApplication loanApplication) {
        List<String> errors = new ArrayList<>();

        if (isInvalidName(loanApplication.getFirstName())) {
            errors.add("Некорректное имя.");
        }

        if (isInvalidName(loanApplication.getLastName())) {
            errors.add("Некорректная фамилия.");
        }

        if (loanApplication.getMiddleName() != null && isInvalidName(loanApplication.getMiddleName())) {
            errors.add("Некорректное отчество.");
        }

        if (loanApplication.getAmount() == null || loanApplication.getAmount().compareTo(BigDecimal.valueOf(10000)) < 0) {
            errors.add("Размер займа меньше минимально допустимого.");
        }

        if (loanApplication.getTerm() == null || loanApplication.getTerm() < 6) {
            errors.add("Некорректный срок кредита.");
        }

        if (!isValidAge(loanApplication.getBirthdate())) {
            errors.add("Недопустимый возраст клиента.");
        }

        if (!isValidEmail(loanApplication.getEmail())) {
            errors.add("Некорректный адрес электронной почты.");
        }

        if (!isValidPassportSeries(loanApplication.getPassportSeries())) {
            errors.add("Некорректные данные паспорта, серия.");
        }

        if (!isValidPassportNumber(loanApplication.getPassportNumber())) {
            errors.add("Некорректные данные паспорта, номер.");
        }

        return errors;
    }

    private boolean isInvalidName(String name) {
        return name == null || !name.matches("[A-Za-z]{2,30}") && !name.matches("[А-Яа-яЁё]{2,30}");
    }

    private boolean isValidAge(LocalDate birthdate) {
        if (birthdate == null) {
            return false;
        }
        LocalDate today = LocalDate.now();
        return Period.between(birthdate, today).getYears() >= 18;
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "[\\w.]{2,50}@[\\w.]{2,20}";
        return email != null && Pattern.matches(emailPattern, email);
    }

    private boolean isValidPassportSeries(String passportSeries) {
        return passportSeries != null && passportSeries.matches("\\d{4}");
    }

    private boolean isValidPassportNumber(String passportNumber) {
        return passportNumber != null && passportNumber.matches("\\d{6}");
    }
}
