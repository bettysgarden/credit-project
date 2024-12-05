package com.example.deal.service;

import com.example.deal.model.dto.LoanApplicationRequest;
import com.example.deal.model.dto.LoanOfferRequest;
import com.example.deal.model.dto.LoanOfferResponse;
import com.example.deal.model.entity.ApplicationEntity;
import com.example.deal.model.entity.CreditEntity;
import jakarta.validation.Valid;

import java.util.List;

public interface ApplicationManagementService {

    List<LoanOfferResponse> createApplication(@Valid LoanApplicationRequest loanApplicationRequest);

    void setPickedLoanOffer(@Valid LoanOfferRequest loanOfferRequest);

    ApplicationEntity getApplicationById(Long applicationId);

    void setCredit(ApplicationEntity applicationEntity, CreditEntity creditEntity);

}
