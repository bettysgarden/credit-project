package com.example.deal.service;

import com.example.deal.model.dto.LoanApplicationRequest;
import jakarta.validation.Valid;

public interface ApplicationService {

    Long createApplication(@Valid LoanApplicationRequest loanApplicationRequest, Long clientId);
}
