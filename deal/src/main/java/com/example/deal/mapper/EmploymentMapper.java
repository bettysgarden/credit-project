package com.example.deal.mapper;

import com.example.credit.application.model.EmploymentDTO;
import com.example.deal.model.Employment;
import org.mapstruct.Mapper;


@Mapper
public interface EmploymentMapper {
    Employment toEntity(EmploymentDTO employment);

    EmploymentDTO toDTO(Employment employment);
}
