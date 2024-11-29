package com.example.deal.util;

import com.example.deal.model.entity.PassportEntity;
import com.example.deal.utils.PassportValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class PassportValidatorTest {
    @InjectMocks
    private PassportValidator validator;

    private static Stream<Arguments> passportValidationArguments() {
        return Stream.of(
                arguments(
                        "Корректный паспорт",
                        createPassport("1234", "123456", "123-456", createDate(2000, 1, 1)),
                        List.of()
                ),
                arguments(
                        "Паспорт пустой",
                        null,
                        List.of("Паспорт не может быть пустым.")
                ),
                arguments(
                        "Некорректная серия паспорта",
                        createPassport("12", "123456", "123-456", createDate(2000, 1, 1)),
                        List.of("Серия паспорта должна состоять из 4 цифр.")
                ),
                arguments(
                        "Некорректный номер паспорта",
                        createPassport("1234", "12345", "123-456", createDate(2000, 1, 1)),
                        List.of("Номер паспорта должен состоять из 6 цифр.")
                ),
                arguments(
                        "Некорректный код подразделения",
                        createPassport("1234", "123456", "12-3456", createDate(2000, 1, 1)),
                        List.of("Код подразделения должен быть в формате XXX-XXX.")
                ),
                arguments(
                        "Дата выдачи в будущем",
                        createPassport("1234", "123456", "123-456", createDate(2030, 1, 1)),
                        List.of("Дата выдачи паспорта не может быть в будущем.")
                ),
                arguments(
                        "Несколько ошибок",
                        createPassport("12", "12345", "12-3456", createDate(2030, 1, 1)),
                        List.of(
                                "Серия паспорта должна состоять из 4 цифр.",
                                "Номер паспорта должен состоять из 6 цифр.",
                                "Код подразделения должен быть в формате XXX-XXX.",
                                "Дата выдачи паспорта не может быть в будущем."
                        )
                )
        );
    }

    private static PassportEntity createPassport(String series, String number, String issueBranch, Date issueDate) {
        PassportEntity passport = new PassportEntity();
        passport.setSeries(series);
        passport.setNumber(number);
        passport.setIssueBranch(issueBranch);
        passport.setIssueDate(issueDate);
        return passport;
    }

    private static Date createDate(int year, int month, int day) {
        LocalDate localDate = LocalDate.of(year, month, day);
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("passportValidationArguments")
    void validatePassport(String description, PassportEntity passport, List<String> expectedErrors) {
        List<String> actualErrors = validator.validatePassport(passport);
        assertEquals(expectedErrors, actualErrors);
    }
}
