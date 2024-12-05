package ru.test.conveyor.mapper;

import com.example.credit.application.model.ScoringDataDTO;
import org.mapstruct.Mapper;
import ru.test.conveyor.model.entity.ScoringData;

@Mapper
public interface ScoringDataMapper {
    ScoringData toEntity(ScoringDataDTO scoringDataDTO);
}
