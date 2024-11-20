package com.example.deal.service;

import com.example.deal.model.dto.LoanApplicationRequest;
import com.example.deal.model.dto.LoanOfferRequest;
import jakarta.validation.Valid;

public interface ApplicationService {

    Long createApplication(@Valid LoanApplicationRequest loanApplicationRequest, Long clientId);

    void updateApplication(@Valid LoanOfferRequest loanOfferRequest);
}
