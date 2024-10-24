package ru.test.conveyor;

import com.example.credit.application.model.EmploymentDTO;
import com.example.credit.application.model.ScoringDataDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.test.conveyor.entity.Employment;
import ru.test.conveyor.entity.ScoringData;
import ru.test.conveyor.enums.EmploymentStatus;
import ru.test.conveyor.enums.Gender;
import ru.test.conveyor.enums.MaritalStatus;
import ru.test.conveyor.mapper.ScoringDataMapper;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ScoringDataMapperTest {

    @Autowired
    private ScoringDataMapper scoringDataMapper;

    private static void compare(ScoringData scoringData, ScoringDataDTO scoringDataDTO) {
        assertEquals(scoringData.getAmount(), scoringDataDTO.getAmount());
        assertEquals(scoringData.getTerm(), scoringDataDTO.getTerm());
        assertEquals(scoringData.getFirstName(), scoringDataDTO.getFirstName());
        assertEquals(scoringData.getLastName(), scoringDataDTO.getLastName());
        assertEquals(ScoringDataDTO.GenderEnum.MALE, scoringDataDTO.getGender());
        assertEquals(scoringData.getBirthdate(), scoringDataDTO.getBirthdate());
        assertEquals(scoringData.getPassportSeries(), scoringDataDTO.getPassportSeries());
        assertEquals(scoringData.getPassportNumber(), scoringDataDTO.getPassportNumber());
        assertEquals(scoringData.getPassportIssueDate(), scoringDataDTO.getPassportIssueDate());
        assertEquals(scoringData.getPassportIssueBranch(), scoringDataDTO.getPassportIssueBranch());
        assertEquals(ScoringDataDTO.MaritalStatusEnum.MARRIED, scoringDataDTO.getMaritalStatus());
        assertEquals(scoringData.getDependentAmount(), scoringDataDTO.getDependentAmount());
        assertNotNull(scoringDataDTO.getEmployment());
        assertEquals(scoringData.getEmployment().getSalary(), scoringDataDTO.getEmployment().getSalary());
        assertEquals(scoringData.getEmployment().getWorkExperienceTotal(), scoringDataDTO.getEmployment().getWorkExperienceTotal());
        assertEquals(scoringData.getEmployment().getWorkExperienceCurrent(), scoringDataDTO.getEmployment().getWorkExperienceCurrent());
        assertEquals(scoringData.getAccount(), scoringDataDTO.getAccount());
        assertEquals(scoringData.getIsInsuranceEnabled(), scoringDataDTO.getIsInsuranceEnabled());
        assertEquals(scoringData.getIsSalaryClient(), scoringDataDTO.getIsSalaryClient());
    }

    @Test
    void testToEntity() {
        EmploymentDTO employment = new EmploymentDTO();
        employment.setEmploymentStatus(EmploymentDTO.EmploymentStatusEnum.EMPLOYED);
        employment.setEmployerINN("1234567890");
        employment.setSalary(BigDecimal.valueOf(50000));
        employment.setPosition(EmploymentDTO.PositionEnum.MANAGER);
        employment.setWorkExperienceTotal(10);
        employment.setWorkExperienceCurrent(5);

        ScoringDataDTO dto = new ScoringDataDTO();
        dto.setAmount(BigDecimal.valueOf(100000));
        dto.setTerm(12);
        dto.setFirstName("Ivan");
        dto.setLastName("Petrov");
        dto.setGender(ScoringDataDTO.GenderEnum.MALE);
        dto.setBirthdate(LocalDate.of(1985, 4, 12));
        dto.setPassportSeries("1234");
        dto.setPassportNumber("567890");
        dto.setPassportIssueDate(LocalDate.of(2010, 1, 1));
        dto.setPassportIssueBranch("Москва");
        dto.setMaritalStatus(ScoringDataDTO.MaritalStatusEnum.MARRIED);
        dto.setDependentAmount(2);
        dto.setEmployment(employment);
        dto.setAccount("1234567890");
        dto.setIsInsuranceEnabled(true);
        dto.setIsSalaryClient(false);

        ScoringData entity = scoringDataMapper.toEntity(dto);

        assertNotNull(entity);
        compare(entity, dto);
    }

    @Test
    void testToDTO() {
        Employment employment = new Employment();
        employment.setSalary(BigDecimal.valueOf(50000));
        employment.setEmploymentStatus(EmploymentStatus.EMPLOYED);
        employment.setWorkExperienceTotal(24);
        employment.setWorkExperienceCurrent(12);

        ScoringData entity = new ScoringData();
        entity.setAmount(BigDecimal.valueOf(100000));
        entity.setTerm(12);
        entity.setFirstName("Ivan");
        entity.setLastName("Petrov");
        entity.setGender(Gender.MALE);
        entity.setBirthdate(LocalDate.of(1985, 4, 12));
        entity.setPassportSeries("1234");
        entity.setPassportNumber("567890");
        entity.setPassportIssueDate(LocalDate.of(2010, 1, 1));
        entity.setPassportIssueBranch("Москва");
        entity.setMaritalStatus(MaritalStatus.MARRIED);
        entity.setDependentAmount(2);
        entity.setEmployment(employment);
        entity.setAccount("1234567890");
        entity.setIsInsuranceEnabled(true);
        entity.setIsSalaryClient(false);
        ScoringDataDTO dto = scoringDataMapper.toDTO(entity);

        assertNotNull(dto);
        compare(entity, dto);
    }
}
