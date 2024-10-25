package ru.test.conveyor.mapper;

import com.example.credit.application.model.CreditDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.test.conveyor.model.entity.Credit;

@Mapper
public interface CreditMapper {
    CreditMapper INSTANCE = Mappers.getMapper(CreditMapper.class);

    CreditDTO toDto(Credit credit);
}
