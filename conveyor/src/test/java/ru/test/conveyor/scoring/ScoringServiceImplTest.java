package ru.test.conveyor.scoring;

import com.example.credit.application.model.CreditDTO;
import com.example.credit.application.model.ScoringDataDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.test.conveyor.enums.EmploymentStatus;
import ru.test.conveyor.enums.Gender;
import ru.test.conveyor.enums.MaritalStatus;
import ru.test.conveyor.exception.CreditDeclinedException;
import ru.test.conveyor.exception.InvalidScoringDataException;
import ru.test.conveyor.mapper.CreditMapper;
import ru.test.conveyor.mapper.ScoringDataMapper;
import ru.test.conveyor.model.entity.Credit;
import ru.test.conveyor.model.entity.Employment;
import ru.test.conveyor.model.entity.ScoringData;
import ru.test.conveyor.service.ScoringServiceImpl;
import ru.test.conveyor.util.ScoringDataValidator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ScoringServiceImplTest {

    @InjectMocks
    private ScoringServiceImpl scoringService;

    @Mock
    private ScoringDataMapper scoringDataMapper;

    @Mock
    private CreditMapper creditMapper;

    @Mock
    private ScoringDataValidator validator;

    private ScoringDataDTO validScoringDataDTO;
    private ScoringData validScoringData;
    private CreditDTO expectedCreditDTO;

    @BeforeEach
    void setUp() {

        Employment employment = new Employment();
        employment.setEmploymentStatus(EmploymentStatus.EMPLOYED);

        validScoringDataDTO = new ScoringDataDTO();

        validScoringData = new ScoringData();
        validScoringData.setEmployment(employment);
        validScoringData.setAmount(new BigDecimal("100000"));
        validScoringData.setTerm(12);
        validScoringData.setMaritalStatus(MaritalStatus.SINGLE);
        validScoringData.setGender(Gender.MALE);
        validScoringData.setBirthdate(LocalDate.of(1990, 1, 1));
        validScoringData.setDependentAmount(0);

        expectedCreditDTO = new CreditDTO();

    }

    @Test
    void testCreditCalculation_ValidData() {
        when(scoringDataMapper.toEntity(validScoringDataDTO)).thenReturn(validScoringData);
        when(validator.validateScoringData(validScoringData)).thenReturn(Collections.emptyList());
        when(creditMapper.toDto(any(Credit.class))).thenReturn(expectedCreditDTO);

        CreditDTO actualCredit = scoringService.getCreditCalculation(validScoringDataDTO);

        assertNotNull(actualCredit);
        assertEquals(expectedCreditDTO, actualCredit);
        verify(scoringDataMapper).toEntity(validScoringDataDTO);
        verify(validator).validateScoringData(validScoringData);
    }

    @Test
    void getCreditCalculation_InvalidData_ThrowsInvalidScoringDataException() {
        List<String> validationErrors = List.of("Invalid birth date");
        when(scoringDataMapper.toEntity(validScoringDataDTO)).thenReturn(validScoringData);
        when(validator.validateScoringData(validScoringData)).thenReturn(validationErrors);

        InvalidScoringDataException exception = assertThrows(
                InvalidScoringDataException.class,
                () -> scoringService.getCreditCalculation(validScoringDataDTO)
        );

        assertEquals(validationErrors, exception.getErrors());
        verify(scoringDataMapper).toEntity(validScoringDataDTO);
        verify(validator).validateScoringData(validScoringData);
    }

    @Test
    void scoring_EmploymentStatusUnemployed_ThrowsCreditDeclinedException() {
        validScoringData.getEmployment().setEmploymentStatus(EmploymentStatus.UNEMPLOYED);

        CreditDeclinedException exception = assertThrows(
                CreditDeclinedException.class,
                () -> scoringService.scoring(validScoringData)
        );

        assertTrue(exception.getMessage().contains("Заявка на кредит отклонена"));
    }
}
