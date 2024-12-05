import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import ru.test.application.model.entity.LoanApplication;
import ru.test.application.util.LoanApplicationValidator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class LoanValidatorTest {

    @InjectMocks
    private LoanApplicationValidator validator;

    private static Stream<Arguments> getArguments() {
        return Stream.of(
                arguments(
                        "Корректная заявка",
                        createLoanApplication("Ivan", BigDecimal.valueOf(100000), LocalDate.of(1990, 1, 1), "test@example.com", "1234", "567890"),
                        List.of()
                ),
                arguments(
                        "Некорректное имя",
                        createLoanApplication("I", BigDecimal.valueOf(100000), LocalDate.of(1990, 1, 1), "test@example.com", "1234", "567890"),
                        List.of("Некорректное имя.")
                ),
                arguments(
                        "Сумма меньше допустимой",
                        createLoanApplication("Ivan", BigDecimal.valueOf(9999), LocalDate.of(1990, 1, 1), "test@example.com", "1234", "567890"),
                        List.of("Размер займа меньше минимально допустимого.")
                ),
                arguments(
                        "Неправильный email",
                        createLoanApplication("Ivan", BigDecimal.valueOf(100000), LocalDate.of(1990, 1, 1), "invalid-email", "1234", "567890"),
                        List.of("Некорректный адрес электронной почты.")
                ),
                arguments(
                        "Недопустимый возраст",
                        createLoanApplication("Ivan", BigDecimal.valueOf(100000), LocalDate.now().minusYears(17), "test@example.com", "1234", "567890"),
                        List.of("Недопустимый возраст клиента.")
                ),
                arguments(
                        "Некорректная серия паспорта",
                        createLoanApplication("Ivan", BigDecimal.valueOf(100000), LocalDate.of(1990, 1, 1), "test@example.com", "12", "567890"),
                        List.of("Некорректные данные паспорта, серия.")
                ),
                arguments(
                        "Некорректный номер паспорта",
                        createLoanApplication("Ivan", BigDecimal.valueOf(100000), LocalDate.of(1990, 1, 1), "test@example.com", "1234", "56789"),
                        List.of("Некорректные данные паспорта, номер.")
                )
        );
    }

    private static LoanApplication createLoanApplication(String firstName, BigDecimal amount, LocalDate birthdate, String email, String passportSeries, String passportNumber) {
        LoanApplication loanApplication = new LoanApplication();
        loanApplication.setFirstName(firstName);
        loanApplication.setLastName("Иванов");
        loanApplication.setMiddleName("Иванович");
        loanApplication.setAmount(amount);
        loanApplication.setTerm(12);
        loanApplication.setBirthdate(birthdate);
        loanApplication.setEmail(email);
        loanApplication.setPassportSeries(passportSeries);
        loanApplication.setPassportNumber(passportNumber);
        return loanApplication;
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @ParameterizedTest(name = "Проверка валидации заявки - {0}")
    @MethodSource("getArguments")
    void validateLoanApplication(String description, LoanApplication loanApplication, List<String> expectedErrors) {
        List<String> errors = validator.validate(loanApplication);
        assertEquals(expectedErrors, errors, description);
    }
}
