package com.example.deal.service;

import com.example.deal.model.dto.LoanApplicationRequest;
import jakarta.validation.Valid;

public interface ClientService {
    Long createClient(@Valid LoanApplicationRequest loanApplicationRequest);
}

