package ru.test.conveyor.mapper.enums;

import com.example.credit.application.model.EmploymentDTO;
import org.mapstruct.Mapper;
import ru.test.conveyor.enums.EmploymentStatus;

@Mapper
public class EmploymentStatusMapper {

    public EmploymentStatus toEntity(EmploymentDTO.EmploymentStatusEnum statusEnum) {
        if (statusEnum == null) {
            return null;
        }
        return switch (statusEnum) {
            case EMPLOYED -> EmploymentStatus.EMPLOYED;
            case SELF_EMPLOYED -> EmploymentStatus.SELF_EMPLOYED;
            case UNEMPLOYED -> EmploymentStatus.UNEMPLOYED;
            case RETIRED -> EmploymentStatus.RETIRED;
            case BUSINESS_OWNER -> EmploymentStatus.BUSINESS_OWNER;
        };
    }

    public EmploymentDTO.EmploymentStatusEnum toDTO(EmploymentStatus status) {
        if (status == null) {
            return null;
        }
        return switch (status) {
            case EMPLOYED -> EmploymentDTO.EmploymentStatusEnum.EMPLOYED;
            case SELF_EMPLOYED -> EmploymentDTO.EmploymentStatusEnum.SELF_EMPLOYED;
            case UNEMPLOYED -> EmploymentDTO.EmploymentStatusEnum.UNEMPLOYED;
            case RETIRED -> EmploymentDTO.EmploymentStatusEnum.RETIRED;
            case BUSINESS_OWNER -> EmploymentDTO.EmploymentStatusEnum.BUSINESS_OWNER;
        };
    }
}
