package com.example.deal.service;

import com.example.deal.model.dto.LoanApplicationRequest;
import com.example.deal.model.dto.LoanOfferResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface OfferService {
    List<LoanOfferResponse> getLoanOffers(@Valid LoanApplicationRequest loanApplicationRequest);
}
