package ru.test.conveyor.mapper;

import com.example.credit.application.model.ScoringDataDTO;
import org.mapstruct.Mapper;
import ru.test.conveyor.entity.ScoringData;
import ru.test.conveyor.mapper.enums.GenderMapper;
import ru.test.conveyor.mapper.enums.MaritalStatusMapper;

@Mapper(uses = {MaritalStatusMapper.class, GenderMapper.class})
public interface ScoringDataMapper {
    ScoringData toEntity(ScoringDataDTO scoringDataDTO);

    ScoringDataDTO toDTO(ScoringData scoringData);
}
