package ru.test.conveyor;

import com.example.credit.application.model.CreditDTO;
import com.example.credit.application.model.EmploymentDTO;
import com.example.credit.application.model.ScoringDataDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.test.conveyor.entity.Credit;
import ru.test.conveyor.mapper.CreditMapper;
import ru.test.conveyor.mapper.ScoringDataMapper;
import ru.test.conveyor.service.ScoringServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ScoringServiceTest {

    @InjectMocks
    private ScoringServiceImpl scoringService;

    @Autowired
    private ScoringDataMapper scoringDataMapper;
    @Autowired
    private CreditMapper creditMapper;

    private ScoringDataDTO scoringDataDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        scoringService = new ScoringServiceImpl(scoringDataMapper, creditMapper);

        EmploymentDTO employment = new EmploymentDTO();
        employment.setEmploymentStatus(EmploymentDTO.EmploymentStatusEnum.EMPLOYED);
        employment.setEmployerINN("1234567890");
        employment.setSalary(BigDecimal.valueOf(50000));
        employment.setPosition(EmploymentDTO.PositionEnum.MANAGER);
        employment.setWorkExperienceTotal(13);
        employment.setWorkExperienceCurrent(5);

        scoringDataDTO = new ScoringDataDTO();
        scoringDataDTO.setFirstName("Ivan");
        scoringDataDTO.setLastName("Petrov");
        scoringDataDTO.setMiddleName("ßIvanovich");
        scoringDataDTO.setGender(ScoringDataDTO.GenderEnum.MALE);
        scoringDataDTO.setBirthdate(LocalDate.of(1985, 4, 12));
        scoringDataDTO.setPassportSeries("1234");
        scoringDataDTO.setPassportNumber("567890");
        scoringDataDTO.setPassportIssueDate(LocalDate.of(2010, 5, 15));
        scoringDataDTO.setPassportIssueBranch("Branch #1");
        scoringDataDTO.setMaritalStatus(ScoringDataDTO.MaritalStatusEnum.MARRIED);
        scoringDataDTO.setDependentAmount(2);
        scoringDataDTO.setAccount("1234567890123456");
        scoringDataDTO.setIsInsuranceEnabled(true);
        scoringDataDTO.setIsSalaryClient(false);
        scoringDataDTO.setEmployment(employment);
        scoringDataDTO.setAmount(BigDecimal.valueOf(100000));
        scoringDataDTO.setTerm(12);
    }

    @Test
    void testCreditCalculation_ValidData() {

        scoringDataDTO.setAmount(BigDecimal.valueOf(100000));
        scoringDataDTO.setTerm(12);
        Credit expectedCredit = new Credit(BigDecimal.valueOf(100000), 12, BigDecimal.valueOf(9000), BigDecimal.valueOf(12), BigDecimal.valueOf(108000), false, false, null);

        CreditDTO actualCredit = scoringService.getCreditCalculation(scoringDataDTO);

        assertNotNull(actualCredit);
        assertEquals(expectedCredit.getAmount(), actualCredit.getAmount());
        assertEquals(expectedCredit.getTerm(), actualCredit.getTerm());
    }

    @Test
    void testScoring_AgeUnderLimit_ShouldThrowException() {
        scoringDataDTO.setAmount(BigDecimal.valueOf(100000));
        scoringDataDTO.setTerm(12);
        scoringDataDTO.setBirthdate(LocalDate.now().minusYears(18));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> scoringService.getCreditCalculation(scoringDataDTO));

        assertTrue(exception.getMessage().contains("Client is declined based on initial checks"));
    }

    @Test
    void testScoring_EmploymentStatusUnemployed_ShouldThrowException() {
        scoringDataDTO.setAmount(BigDecimal.valueOf(100000));
        scoringDataDTO.setTerm(12);
        scoringDataDTO.getEmployment().setEmploymentStatus(EmploymentDTO.EmploymentStatusEnum.UNEMPLOYED);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> scoringService.getCreditCalculation(scoringDataDTO));

        assertTrue(exception.getMessage().contains("Client is unemployed, scoring is declined"));
    }

    @Test
    void testIsDeclined_InvalidExperience_ShouldReturnTrue() {
        scoringDataDTO.setAmount(BigDecimal.valueOf(100000));
        scoringDataDTO.setTerm(12);
        scoringDataDTO.getEmployment().setWorkExperienceTotal(6);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> scoringService.getCreditCalculation(scoringDataDTO));

        assertTrue(exception.getMessage().contains("Client is declined based on initial checks"));
    }
}
