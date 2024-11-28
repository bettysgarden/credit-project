package com.example.deal.service;

import com.example.deal.model.dto.LoanApplicationRequest;
import com.example.deal.model.dto.LoanOfferRequest;
import com.example.deal.model.dto.LoanOfferResponse;
import com.example.deal.model.entity.ApplicationEntity;
import com.example.deal.model.entity.jsonb.AppliedLoanOffer;
import jakarta.validation.Valid;

import java.util.List;

public interface LoanOfferService {
    List<LoanOfferResponse> getLoanOffers(@Valid LoanApplicationRequest loanApplicationRequest, Integer applicationId);

    AppliedLoanOffer setPickedLoanOffer(LoanOfferRequest loanOfferRequest, ApplicationEntity applicationEntity);
}
