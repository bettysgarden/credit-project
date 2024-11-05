package com.example.deal.mapper;

import com.example.credit.application.model.FinishRegistrationRequestDTO;
import com.example.deal.model.entity.FinishRegistrationRequest;
import org.mapstruct.Mapper;

@Mapper
public interface FinishRegistrationMapper {
    FinishRegistrationRequest toEntity(FinishRegistrationRequestDTO dto);

    FinishRegistrationRequestDTO toDTO(FinishRegistrationRequest entity);
}
