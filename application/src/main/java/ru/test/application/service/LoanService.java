package ru.test.application.service;

import jakarta.validation.Valid;
import ru.test.application.model.dto.LoanApplicationRequest;
import ru.test.application.model.dto.LoanOfferRequest;
import ru.test.application.model.dto.LoanOfferResponse;

import java.util.List;

public interface LoanService {
    List<LoanOfferResponse> getLoanOffers(LoanApplicationRequest loanApplicationDTO);

    void updateLoanOfferForApplication(@Valid LoanOfferRequest loanOfferRequest);
}
