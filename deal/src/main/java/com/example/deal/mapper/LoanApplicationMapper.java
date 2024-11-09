package com.example.deal.mapper;

import com.example.credit.application.model.LoanApplicationRequestDTO;
import com.example.deal.model.entity.ApplicationEntity;
import org.mapstruct.Mapper;

@Mapper
public interface LoanApplicationMapper {
    ApplicationEntity toEntity(LoanApplicationRequestDTO loanApplicationRequestDTO);

    LoanApplicationRequestDTO toDTO(ApplicationEntity loanApplication);
}
