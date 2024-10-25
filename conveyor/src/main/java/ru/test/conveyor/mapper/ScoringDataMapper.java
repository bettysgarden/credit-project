package ru.test.conveyor.mapper;

import com.example.credit.application.model.ScoringDataDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.test.conveyor.model.entity.ScoringData;

@Mapper
public interface ScoringDataMapper {

    @Mapping(source = "employment.position", target = "employment.position")
    @Mapping(source = "employment.employmentStatus", target = "employment.employmentStatus")
    @Mapping(source = "maritalStatus", target = "maritalStatus")
    @Mapping(source = "gender", target = "gender")
    ScoringData toEntity(ScoringDataDTO scoringDataDTO);
}
