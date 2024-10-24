package ru.test.conveyor.mapper;

import com.example.credit.application.model.LoanApplicationRequestDTO;
import org.mapstruct.Mapper;
import ru.test.conveyor.entity.LoanApplication;

@Mapper
public interface LoanApplicationMapper {
    LoanApplication toEntity(LoanApplicationRequestDTO loanApplicationRequestDTO);

    LoanApplicationRequestDTO toDTO(LoanApplication loanApplication);
}
