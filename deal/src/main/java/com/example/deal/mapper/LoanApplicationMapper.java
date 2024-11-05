package com.example.deal.mapper;

import com.example.credit.application.model.LoanApplicationRequestDTO;
import com.example.deal.model.entity.LoanApplication;
import org.mapstruct.Mapper;

@Mapper
public interface LoanApplicationMapper {
    LoanApplication toEntity(LoanApplicationRequestDTO loanApplicationRequestDTO);

    LoanApplicationRequestDTO toDTO(LoanApplication loanApplication);
}
