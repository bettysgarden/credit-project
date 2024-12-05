import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.test.application.mapper.LoanApplicationMapper;
import ru.test.application.model.dto.LoanApplicationRequest;
import ru.test.application.model.entity.LoanApplication;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LoanApplicationMapperTest {
    private final LoanApplicationMapper mapper = Mappers.getMapper(LoanApplicationMapper.class);

    @Test
    void toEntity_shouldMapLoanApplicationRequestToLoanApplication() {
        LoanApplicationRequest request = new LoanApplicationRequest();
        request.setAmount(new BigDecimal("50000"));
        request.setTerm(24);
        request.setFirstName("Иван");
        request.setLastName("Иванов");
        request.setMiddleName("Иванович");
        request.setEmail("ivan.ivanov@example.com");
        request.setBirthdate(LocalDate.of(1990, 1, 1));
        request.setPassportSeries("1234");
        request.setPassportNumber("567890");

        LoanApplication loanApplication = mapper.toEntity(request);

        assertNotNull(loanApplication);
        assertEquals(request.getAmount(), loanApplication.getAmount());
        assertEquals(request.getTerm(), loanApplication.getTerm());
        assertEquals(request.getFirstName(), loanApplication.getFirstName());
        assertEquals(request.getLastName(), loanApplication.getLastName());
        assertEquals(request.getMiddleName(), loanApplication.getMiddleName());
        assertEquals(request.getEmail(), loanApplication.getEmail());
        assertEquals(request.getBirthdate(), loanApplication.getBirthdate());
        assertEquals(request.getPassportSeries(), loanApplication.getPassportSeries());
        assertEquals(request.getPassportNumber(), loanApplication.getPassportNumber());
    }
}
