package ru.test.conveyor.mapper;

import com.example.credit.application.model.LoanApplicationRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.test.conveyor.model.entity.LoanApplication;

@Mapper
public interface LoanApplicationMapper {
    LoanApplicationMapper INSTANCE = Mappers.getMapper(LoanApplicationMapper.class);

    LoanApplication toEntity(LoanApplicationRequestDTO loanApplicationRequestDTO);
}
