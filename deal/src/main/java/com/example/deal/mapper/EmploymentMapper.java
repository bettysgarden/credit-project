package com.example.deal.mapper;

import com.example.credit.application.model.EmploymentDTO;
import com.example.deal.entity.Employment;
import org.mapstruct.Mapper;


@Mapper(uses = {EmploymentStatusMapper.class, PositionMapper.class})
public interface EmploymentMapper {
    Employment toEntity(EmploymentDTO employment);

    EmploymentDTO toDTO(Employment employment);
}
