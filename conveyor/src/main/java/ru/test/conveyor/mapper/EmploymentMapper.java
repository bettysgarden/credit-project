package ru.test.conveyor.mapper;

import com.example.credit.application.model.EmploymentDTO;
import org.mapstruct.Mapper;
import ru.test.conveyor.entity.Employment;

@Mapper
public interface EmploymentMapper {
    Employment toEntity(Employment employment);

    EmploymentDTO toDTO(Employment employment);
}
