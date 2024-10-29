package ru.test.conveyor.mapper;

import com.example.credit.application.model.LoanApplicationRequestDTO;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.test.conveyor.model.entity.LoanApplication;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LoanApplicationMapperTest {
    private final LoanApplicationMapper mapper = Mappers.getMapper(LoanApplicationMapper.class);

    @Test
    void toEntity_shouldMapLoanApplicationRequestDTOToLoanApplication() {
        LoanApplicationRequestDTO requestDTO = new LoanApplicationRequestDTO();
        requestDTO.setAmount(new BigDecimal("50000"));
        requestDTO.setTerm(24);
        requestDTO.setFirstName("Иван");
        requestDTO.setLastName("Иванов");
        requestDTO.setMiddleName("Иванович");
        requestDTO.setEmail("ivan.ivanov@example.com");
        requestDTO.setBirthdate(LocalDate.of(1990, 1, 1));
        requestDTO.setPassportSeries("1234");
        requestDTO.setPassportNumber("567890");

        LoanApplication loanApplication = mapper.toEntity(requestDTO);

        assertNotNull(loanApplication);
        assertEquals(requestDTO.getAmount(), loanApplication.getAmount());
        assertEquals(requestDTO.getTerm(), loanApplication.getTerm());
        assertEquals(requestDTO.getFirstName(), loanApplication.getFirstName());
        assertEquals(requestDTO.getLastName(), loanApplication.getLastName());
        assertEquals(requestDTO.getMiddleName(), loanApplication.getMiddleName());
        assertEquals(requestDTO.getEmail(), loanApplication.getEmail());
        assertEquals(requestDTO.getBirthdate(), loanApplication.getBirthdate());
        assertEquals(requestDTO.getPassportSeries(), loanApplication.getPassportSeries());
        assertEquals(requestDTO.getPassportNumber(), loanApplication.getPassportNumber());
    }
}
