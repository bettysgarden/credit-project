package com.example.deal.mapper;

import com.example.credit.application.model.FinishRegistrationRequestDTO;
import com.example.deal.enums.MaritalStatus;
import org.mapstruct.Mapper;

@Mapper
public class MaritalStatusMapper {
    MaritalStatus toEntiy(FinishRegistrationRequestDTO.MaritalStatusEnum maritalStatusEnum) {
        if (maritalStatusEnum == null) {
            return null;
        }
        return switch (maritalStatusEnum) {
            case MARRIED -> MaritalStatus.MARRIED;
            case DIVORCED -> MaritalStatus.DIVORCED;
            case SINGLE -> MaritalStatus.SINGLE;
        };
    }

    FinishRegistrationRequestDTO.MaritalStatusEnum toDto(MaritalStatus maritalStatus) {
        if (maritalStatus == null) {
            return null;
        }
        return switch (maritalStatus) {
            case MARRIED -> FinishRegistrationRequestDTO.MaritalStatusEnum.MARRIED;
            case DIVORCED -> FinishRegistrationRequestDTO.MaritalStatusEnum.DIVORCED;
            case SINGLE -> FinishRegistrationRequestDTO.MaritalStatusEnum.SINGLE;
        };
    }
}
