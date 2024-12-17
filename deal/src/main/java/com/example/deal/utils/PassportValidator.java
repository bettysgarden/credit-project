package com.example.deal.utils;

import com.example.deal.model.entity.PassportEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class PassportValidator {

    public List<String> validatePassport(PassportEntity passport) {
        List<String> errors = new ArrayList<>();

        if (passport == null) {
            errors.add("Паспорт не может быть пустым.");
            return errors;
        }

        if (!isValidSeries(passport.getSeries())) {
            errors.add("Серия паспорта должна состоять из 4 цифр.");
        }

        if (!isValidNumber(passport.getNumber())) {
            errors.add("Номер паспорта должен состоять из 6 цифр.");
        }

        if (passport.getIssueBranch() != null && !isValidIssueBranch(passport.getIssueBranch())) {
            errors.add("Код подразделения должен быть в формате XXX-XXX.");
        }

        if (passport.getIssueDate() != null && !isValidIssueDate(passport.getIssueDate())) {
            errors.add("Дата выдачи паспорта не может быть в будущем.");
        }

        return errors;
    }

    private boolean isValidSeries(String series) {
        return StringUtils.hasText(series) && series.matches("\\d{4}");
    }

    private boolean isValidNumber(String number) {
        return StringUtils.hasText(number) && number.matches("\\d{6}");
    }

    private boolean isValidIssueBranch(String issueBranch) {
        return StringUtils.hasText(issueBranch) && issueBranch.matches("\\d{3}-\\d{3}");
    }

    private boolean isValidIssueDate(Date issueDate) {
        if (issueDate == null) {
            return false;
        }
        LocalDate issueLocalDate = issueDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return !issueLocalDate.isAfter(LocalDate.now());
    }
}
