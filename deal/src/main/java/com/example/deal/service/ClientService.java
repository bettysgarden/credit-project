package com.example.deal.service;

import com.example.deal.model.dto.FinishRegistrationRequest;
import com.example.deal.model.dto.LoanApplicationRequest;
import com.example.deal.model.dto.ScoringDataRequest;
import com.example.deal.model.entity.ApplicationEntity;
import com.example.deal.model.entity.ClientEntity;
import jakarta.validation.Valid;

public interface ClientService {
    ClientEntity createClient(@Valid LoanApplicationRequest loanApplicationRequest);

    ScoringDataRequest getScoringInformation(ApplicationEntity application, FinishRegistrationRequest finishRegistrationRequest);
}

