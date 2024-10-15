package ru.test.conveyor.mapper;

import com.example.credit.application.model.CreditDTO;
import org.mapstruct.Mapper;
import ru.test.conveyor.entity.Credit;

@Mapper
public interface CreditMapper {
    CreditDTO toDto(Credit entity);

    Credit toEntity(CreditDTO dto);
}
