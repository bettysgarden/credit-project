package ru.test.conveyor.mapper;

import com.example.credit.application.model.ScoringDataDTO;
import org.mapstruct.Mapper;
import ru.test.conveyor.entity.ScoringData;

@Mapper
public interface ScoringDataMapper {
    ScoringData toEntity(ScoringDataDTO scoringDataDTO);

    ScoringDataDTO toDTO(ScoringData scoringData);
}
