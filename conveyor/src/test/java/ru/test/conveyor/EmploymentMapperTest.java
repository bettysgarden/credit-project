package ru.test.conveyor;

import com.example.credit.application.model.EmploymentDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.test.conveyor.entity.Employment;
import ru.test.conveyor.enums.EmploymentStatus;
import ru.test.conveyor.enums.Position;
import ru.test.conveyor.mapper.EmploymentMapper;
import ru.test.conveyor.mapper.enums.EmploymentStatusMapper;
import ru.test.conveyor.mapper.enums.PositionMapper;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class EmploymentMapperTest {
    @Autowired
    private EmploymentMapper employmentMapper;
    @Autowired
    private PositionMapper positionMapper;
    @Autowired
    private EmploymentStatusMapper employmentStatusMapper;

    @Test
    public void testToDTO() {
        Employment entity = new Employment();
        entity.setEmploymentStatus(EmploymentStatus.EMPLOYED);
        entity.setEmployerINN("1234567890");
        entity.setSalary(BigDecimal.valueOf(50000));
        entity.setPosition(Position.MANAGER);
        entity.setWorkExperienceTotal(10);
        entity.setWorkExperienceCurrent(5);

        EmploymentDTO dto = employmentMapper.toDTO(entity);
        assertNotNull(dto);
        compareWithAsserts(dto, entity);

    }

    private void compareWithAsserts(EmploymentDTO dto, Employment entity) {
        assertEquals(dto.getEmployerINN(), entity.getEmployerINN());
        assertEquals(dto.getSalary(), entity.getSalary());
        assertEquals(dto.getWorkExperienceTotal(), entity.getWorkExperienceTotal());
        assertEquals(dto.getWorkExperienceCurrent(), entity.getWorkExperienceCurrent());

        Position actualEntityPosition = positionMapper.toEntity(dto.getPosition());
        assertEquals(actualEntityPosition, entity.getPosition());

        EmploymentStatus actualEmploymentStatus = employmentStatusMapper.toEntity(dto.getEmploymentStatus());
        assertEquals(actualEmploymentStatus, entity.getEmploymentStatus());
    }

    @Test
    public void testToEntity() {
        EmploymentDTO dto = new EmploymentDTO();
        dto.setEmploymentStatus(EmploymentDTO.EmploymentStatusEnum.EMPLOYED);
        dto.setEmployerINN("1234567890");
        dto.setSalary(BigDecimal.valueOf(50000));
        dto.setPosition(EmploymentDTO.PositionEnum.MANAGER);
        dto.setWorkExperienceTotal(10);
        dto.setWorkExperienceCurrent(5);

        Employment entity = employmentMapper.toEntity(dto);
        assertNotNull(entity);
        compareWithAsserts(dto, entity);

    }

}
