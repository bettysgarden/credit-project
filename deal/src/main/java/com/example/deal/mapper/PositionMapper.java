package com.example.deal.mapper;

import com.example.credit.application.model.EmploymentDTO;
import com.example.deal.enums.Position;
import org.mapstruct.Mapper;

@Mapper
public class PositionMapper {

    public Position toEntity(EmploymentDTO.PositionEnum positionEnum) {
        if (positionEnum == null) {
            return null;
        }
        return switch (positionEnum) {
            case MANAGER -> Position.MANAGER;
            case TOP_MANAGER -> Position.TOP_MANAGER;
            case WORKER -> Position.WORKER;
        };
    }

    public EmploymentDTO.PositionEnum toDTO(Position position) {
        if (position == null) {
            return null;
        }

        return switch (position) {
            case MANAGER -> EmploymentDTO.PositionEnum.MANAGER;
            case TOP_MANAGER -> EmploymentDTO.PositionEnum.TOP_MANAGER;
            case WORKER -> EmploymentDTO.PositionEnum.WORKER;
        };
    }
}