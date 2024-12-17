package com.example.deal.service;

import com.example.deal.model.dto.CreditResponse;
import com.example.deal.model.entity.CreditEntity;
import com.example.deal.model.entity.CreditStatusEntity;
import com.example.deal.model.enums.CreditStatusEnum;

public interface CreditRepositoryAdapter {
    CreditEntity saveCredit(CreditResponse creditResponse);

    CreditStatusEntity findStatusByTitle(CreditStatusEnum statusEnum);
}
