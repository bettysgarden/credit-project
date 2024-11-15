package com.example.deal.service;

import com.example.deal.model.dto.LoanApplicationRequest;
import com.example.deal.model.entity.ClientEntity;
import jakarta.validation.Valid;

public interface ClientService {
    Long createClient(@Valid LoanApplicationRequest loanApplicationRequest);

    ClientEntity getClient(Long clientId);

}

