package ru.test.conveyor.mapper.enums;

import com.example.credit.application.model.ScoringDataDTO.GenderEnum;
import org.mapstruct.Mapper;
import ru.test.conveyor.enums.Gender;

@Mapper
public class GenderMapper {

    Gender toEntity(GenderEnum genderEnum) {
        if (genderEnum == null) {
            return null;
        }
        return switch (genderEnum) {
            case MALE -> Gender.MALE;
            case FEMALE -> Gender.FEMALE;
            case NON_BINARY -> Gender.NON_BINARY;
        };
    }

    GenderEnum toDTO(Gender gender) {
        if (gender == null) {
            return null;
        }
        return switch (gender) {
            case MALE -> GenderEnum.MALE;
            case FEMALE -> GenderEnum.FEMALE;
            case NON_BINARY -> GenderEnum.NON_BINARY;
        };
    }
}