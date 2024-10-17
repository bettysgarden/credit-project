package ru.test.conveyor.mapper;

import com.example.credit.application.model.EmploymentDTO;
import org.mapstruct.Mapper;
import ru.test.conveyor.entity.Employment;
import ru.test.conveyor.mapper.enums.EmploymentStatusMapper;
import ru.test.conveyor.mapper.enums.PositionMapper;

@Mapper(uses = {EmploymentStatusMapper.class, PositionMapper.class})
public interface EmploymentMapper {
    Employment toEntity(EmploymentDTO employment);

    EmploymentDTO toDTO(Employment employment);
}
