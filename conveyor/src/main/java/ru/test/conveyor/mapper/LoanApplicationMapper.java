package ru.test.conveyor.mapper;

import com.example.credit.application.model.LoanApplicationRequestDTO;
import org.mapstruct.Mapper;
import ru.test.conveyor.model.entity.LoanApplication;

@Mapper
public interface LoanApplicationMapper {

    LoanApplication toEntity(LoanApplicationRequestDTO loanApplicationRequestDTO);
}
