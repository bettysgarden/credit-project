package com.example.deal.mapper;

import com.example.credit.application.model.FinishRegistrationRequestDTO;
import com.example.deal.enums.Gender;
import org.mapstruct.Mapper;

@Mapper
public class GenderMapper {

    Gender toEntity(FinishRegistrationRequestDTO.GenderEnum genderEnum) {
        if (genderEnum == null) {
            return null;
        }
        return switch (genderEnum) {
            case MALE -> Gender.MALE;
            case FEMALE -> Gender.FEMALE;
            case NON_BINARY -> Gender.NON_BINARY;
        };
    }

    FinishRegistrationRequestDTO.GenderEnum toDTO(Gender gender) {
        if (gender == null) {
            return null;
        }
        return switch (gender) {
            case MALE -> FinishRegistrationRequestDTO.GenderEnum.MALE;
            case FEMALE -> FinishRegistrationRequestDTO.GenderEnum.FEMALE;
            case NON_BINARY -> FinishRegistrationRequestDTO.GenderEnum.NON_BINARY;
        };
    }
}