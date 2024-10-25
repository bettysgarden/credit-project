package ru.test.conveyor.mapper;

import com.example.credit.application.model.ScoringDataDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.test.conveyor.model.entity.ScoringData;

@Mapper
public interface ScoringDataMapper {

    ScoringDataMapper INSTANCE = Mappers.getMapper(ScoringDataMapper.class);

    @Mapping(source = "employment.position", target = "employment.position")
    @Mapping(source = "employment.employmentStatus", target = "employment.employmentStatus")
    @Mapping(source = "maritalStatus", target = "maritalStatus")
    @Mapping(source = "gender", target = "gender")
    ScoringData toEntity(ScoringDataDTO scoringDataDTO);
}
