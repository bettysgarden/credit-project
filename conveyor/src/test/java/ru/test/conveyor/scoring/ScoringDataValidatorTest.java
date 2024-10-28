package ru.test.conveyor.scoring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import ru.test.conveyor.model.entity.Employment;
import ru.test.conveyor.model.entity.ScoringData;
import ru.test.conveyor.util.ScoringDataValidator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class ScoringDataValidatorTest {

    @InjectMocks
    private ScoringDataValidator validator;

    private static Stream<Arguments> getArguments() {
        return Stream.of(
                arguments(
                        "Корректные данные скоринга",
                        createScoringData(LocalDate.of(1985, 1, 1), BigDecimal.valueOf(50000), 15, BigDecimal.valueOf(800000), 24, 0, true, true),
                        List.of()
                ),
                arguments(
                        "Клиент не прошёл по возрасту",
                        createScoringData(LocalDate.of(2005, 1, 1), BigDecimal.valueOf(50000), 15, BigDecimal.valueOf(800000), 24, 0, true, true),
                        List.of("Клиент не прошёл по возрасту.")
                ),
                arguments(
                        "Недостаточный стаж работы",
                        createScoringData(LocalDate.of(1985, 1, 1), BigDecimal.valueOf(50000), 10, BigDecimal.valueOf(800000), 24, 0, true, true),
                        List.of("Недостаточный стаж работы.")
                ),
                arguments(
                        "Сумма кредита превышает допустимое соотношение",
                        createScoringData(LocalDate.of(1985, 1, 1), BigDecimal.valueOf(50000), 15, BigDecimal.valueOf(1200000), 24, 0, true, true),
                        List.of("Сумма кредита превышает допустимое соотношение 20-кратного размера зарплаты.") // Ошибка в сумме кредита
                ),
                arguments(
                        "Неверно указан срок кредита",
                        createScoringData(LocalDate.of(1985, 1, 1), BigDecimal.valueOf(50000), 15, BigDecimal.valueOf(800000), 5, 0, true, true),
                        List.of("Неверно указан срок кредита.")
                ),
                arguments(
                        "Некорректное количество иждивенцев",
                        createScoringData(LocalDate.of(1985, 1, 1), BigDecimal.valueOf(50000), 15, BigDecimal.valueOf(800000), 24, -1, true, true),
                        List.of("Некорректное количество иждивенцев.")
                ),
                arguments(
                        "Не указано, оформлена ли страховка",
                        createScoringData(LocalDate.of(1985, 1, 1), BigDecimal.valueOf(50000), 15, BigDecimal.valueOf(800000), 24, 0, null, true),
                        List.of("Не указано, оформлена ли страховка.")
                ),
                arguments(
                        "Не указано, является ли клиент зарплатным клиентом",
                        createScoringData(LocalDate.of(1985, 1, 1), BigDecimal.valueOf(50000), 15, BigDecimal.valueOf(800000), 24, 0, true, null),
                        List.of("Не указано, является ли клиент зарплатным клиентом.")
                )
        );
    }

    private static ScoringData createScoringData(LocalDate birthdate, BigDecimal salary, int workExperienceTotal, BigDecimal amount, int term, int dependentAmount, Boolean isInsuranceEnabled, Boolean isSalaryClient) {
        Employment employment = new Employment();
        employment.setSalary(salary);
        employment.setWorkExperienceTotal(workExperienceTotal);
        employment.setWorkExperienceCurrent(workExperienceTotal);

        ScoringData scoringData = new ScoringData();
        scoringData.setBirthdate(birthdate);
        scoringData.setEmployment(employment);
        scoringData.setAmount(amount);
        scoringData.setTerm(term);
        scoringData.setDependentAmount(dependentAmount);
        scoringData.setIsInsuranceEnabled(isInsuranceEnabled);
        scoringData.setIsSalaryClient(isSalaryClient);
        return scoringData;
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @ParameterizedTest(name = "Проверка валидации данных скоринга - {0}")
    @MethodSource("getArguments")
    void validateScoringData(String description, ScoringData scoringData, List<String> expectedErrors) {
        List<String> errors = validator.validateScoringData(scoringData);
        assertEquals(expectedErrors, errors, description);
    }
}
