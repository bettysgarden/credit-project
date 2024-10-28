package ru.test.conveyor.scoring;

import com.example.credit.application.model.CreditDTO;
import com.example.credit.application.model.EmploymentDTO;
import com.example.credit.application.model.ScoringDataDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import ru.test.conveyor.exception.CreditDeclinedException;
import ru.test.conveyor.mapper.CreditMapper;
import ru.test.conveyor.mapper.ScoringDataMapper;
import ru.test.conveyor.model.entity.Credit;
import ru.test.conveyor.service.ScoringServiceImpl;
import ru.test.conveyor.util.ScoringDataValidator;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ScoringServiceImplTest {

    @InjectMocks
    private ScoringServiceImpl scoringService;

    @Spy
    private ScoringDataMapper scoringDataMapper = Mappers.getMapper(ScoringDataMapper.class);

    @Spy
    private CreditMapper creditMapper = Mappers.getMapper(CreditMapper.class);

    @Mock
    private ScoringDataValidator validator;

    private ScoringDataDTO scoringDataDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

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
        scoringDataDTO.setMiddleName("Ivanovich");
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
    void testScoring_EmploymentStatusUnemployed_ShouldThrowException() {
        scoringDataDTO.setAmount(BigDecimal.valueOf(100000));
        scoringDataDTO.setTerm(12);
        scoringDataDTO.getEmployment().setEmploymentStatus(EmploymentDTO.EmploymentStatusEnum.UNEMPLOYED);

        assertThrows(CreditDeclinedException.class, () -> scoringService.getCreditCalculation(scoringDataDTO));
    }
}
