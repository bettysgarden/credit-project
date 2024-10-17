package com.example.deal.mapper;

import com.example.credit.application.model.FinishRegistrationRequestDTO;
import com.example.deal.entity.FinishRegistrationRequest;
import org.mapstruct.Mapper;

@Mapper(uses = {EmploymentMapper.class, GenderMapper.class, MaritalStatusMapper.class})
public interface FinishRegistrationMapper {
    FinishRegistrationRequest toEntity(FinishRegistrationRequestDTO dto);

    FinishRegistrationRequestDTO toDTO(FinishRegistrationRequest entity);
}
