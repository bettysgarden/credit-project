package ru.test.application.mapper;

import org.mapstruct.Mapper;
import ru.test.application.model.dto.LoanApplicationRequest;
import ru.test.application.model.entity.LoanApplication;

@Mapper
public interface LoanApplicationMapper {
    LoanApplication toEntity(LoanApplicationRequest applicationRequest);
}
