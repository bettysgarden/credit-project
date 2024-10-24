package ru.test.conveyor.mapper.enums;

import com.example.credit.application.model.ScoringDataDTO;
import org.mapstruct.Mapper;
import ru.test.conveyor.enums.MaritalStatus;

@Mapper
public class MaritalStatusMapper {
    MaritalStatus toEntiy(ScoringDataDTO.MaritalStatusEnum maritalStatusEnum) {
        if (maritalStatusEnum == null) {
            return null;
        }
        return switch (maritalStatusEnum) {
            case MARRIED -> MaritalStatus.MARRIED;
            case DIVORCED -> MaritalStatus.DIVORCED;
            case SINGLE -> MaritalStatus.SINGLE;
        };
    }

    ScoringDataDTO.MaritalStatusEnum toDto(MaritalStatus maritalStatus) {
        if (maritalStatus == null) {
            return null;
        }
        return switch (maritalStatus) {
            case MARRIED -> ScoringDataDTO.MaritalStatusEnum.MARRIED;
            case DIVORCED -> ScoringDataDTO.MaritalStatusEnum.DIVORCED;
            case SINGLE -> ScoringDataDTO.MaritalStatusEnum.SINGLE;
        };
    }
}
