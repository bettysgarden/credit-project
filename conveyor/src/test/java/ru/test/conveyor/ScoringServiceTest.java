package ru.test.conveyor;

import com.example.credit.application.model.CreditDTO;
import com.example.credit.application.model.EmploymentDTO;
import com.example.credit.application.model.ScoringDataDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.test.conveyor.entity.Credit;
import ru.test.conveyor.mapper.CreditMapper;
import ru.test.conveyor.mapper.EmploymentMapper;
import ru.test.conveyor.mapper.ScoringDataMapper;
import ru.test.conveyor.service.ScoringServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ScoringServiceTest {

    @InjectMocks
    private ScoringServiceImpl scoringService;

    @Mock
    private ScoringDataMapper scoringDataMapper;

    @Mock
    private CreditMapper creditMapper;

    @Mock
    private EmploymentMapper employmentMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Disabled
    @Test
    void testCreditCalculation_ValidData() {
        ScoringDataDTO scoringDataDTO = getScoringDataDTO(BigDecimal.valueOf(100000), 12);
        Credit expectedCredit = new Credit(BigDecimal.valueOf(100000), 12, BigDecimal.valueOf(9000), BigDecimal.valueOf(12), BigDecimal.valueOf(108000), false, false, null);

        when(scoringDataMapper.toEntity(any(ScoringDataDTO.class))).thenReturn(null);

        CreditDTO actualCredit = scoringService.getCreditCalculation(scoringDataDTO);

        assertNotNull(actualCredit);
        assertEquals(expectedCredit.getAmount(), actualCredit.getAmount());
        assertEquals(expectedCredit.getTerm(), actualCredit.getTerm());
    }

    @Disabled
    @Test
    void testScoring_AgeUnderLimit_ShouldThrowException() {
        ScoringDataDTO scoringDataDTO = getScoringDataDTO(BigDecimal.valueOf(100000), 12);
        scoringDataDTO.setBirthdate(LocalDate.now().minusYears(18));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            scoringService.getCreditCalculation(scoringDataDTO);
        });

        assertTrue(exception.getMessage().contains("Client is declined based on initial checks"));
    }

    @Disabled
    @Test
    void testScoring_EmploymentStatusUnemployed_ShouldThrowException() {
        ScoringDataDTO scoringDataDTO = getScoringDataDTO(BigDecimal.valueOf(100000), 12);
        scoringDataDTO.getEmployment().setEmploymentStatus(EmploymentDTO.EmploymentStatusEnum.UNEMPLOYED);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            scoringService.getCreditCalculation(scoringDataDTO);
        });

        assertTrue(exception.getMessage().contains("Client is unemployed, scoring is declined"));
    }

    @Disabled
    @Test
    void testScoring_ValidRateCalculation() {
        ScoringDataDTO scoringDataDTO = getScoringDataDTO(BigDecimal.valueOf(100000), 12);
        scoringDataDTO.setGender(ScoringDataDTO.GenderEnum.MALE);
        scoringDataDTO.setBirthdate(LocalDate.now().minusYears(40));

        CreditDTO creditDTO = scoringService.getCreditCalculation(scoringDataDTO);

        assertNotNull(creditDTO);
        assertTrue(creditDTO.getRate().compareTo(BigDecimal.valueOf(7)) < 0);
    }

    @Disabled
    @Test
    void testScoring_DependentAmountAdjustment() {
        ScoringDataDTO scoringDataDTO = getScoringDataDTO(BigDecimal.valueOf(100000), 12);
        scoringDataDTO.setDependentAmount(3);

        CreditDTO creditDTO = scoringService.getCreditCalculation(scoringDataDTO);

        assertNotNull(creditDTO);
        assertTrue(creditDTO.getRate().compareTo(BigDecimal.valueOf(11)) > 0);
    }

    @Disabled
    @Test
    void testIsDeclined_InvalidExperience_ShouldReturnTrue() {
        ScoringDataDTO scoringDataDTO = getScoringDataDTO(BigDecimal.valueOf(100000), 12);
        scoringDataDTO.getEmployment().setWorkExperienceTotal(6); // Меньше 12 месяцев опыта

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            scoringService.getCreditCalculation(scoringDataDTO);
        });

        assertTrue(exception.getMessage().contains("Client is declined based on initial checks"));
    }

    private ScoringDataDTO getScoringDataDTO(BigDecimal amount, int term) {
        EmploymentDTO employment = new EmploymentDTO();
        employment.setEmploymentStatus(EmploymentDTO.EmploymentStatusEnum.EMPLOYED);
        employment.setSalary(BigDecimal.valueOf(50000));
        employment.setWorkExperienceTotal(24);
        employment.setWorkExperienceCurrent(12);

        ScoringDataDTO scoringData = new ScoringDataDTO();
        scoringData.setAmount(amount);
        scoringData.setTerm(term);
        scoringData.setBirthdate(LocalDate.now().minusYears(30));
        scoringData.setGender(ScoringDataDTO.GenderEnum.MALE);
        scoringData.setMaritalStatus(ScoringDataDTO.MaritalStatusEnum.SINGLE);
        scoringData.setEmployment(employment);
        scoringData.setIsInsuranceEnabled(false);
        scoringData.setIsSalaryClient(false);
        return scoringData;
    }
}
