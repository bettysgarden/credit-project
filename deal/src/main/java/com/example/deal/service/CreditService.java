package com.example.deal.service;

import com.example.deal.model.dto.FinishRegistrationRequest;
import jakarta.validation.Valid;

public interface CreditService {
    void calculateCredit(Long applicationId, @Valid FinishRegistrationRequest finishRegistrationRequest);
}
