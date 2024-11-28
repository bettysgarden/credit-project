package com.example.deal.mapper;

import com.example.deal.model.dto.LoanOfferRequest;
import com.example.deal.model.entity.jsonb.AppliedLoanOffer;
import org.mapstruct.Mapper;

@Mapper
public interface LoanOfferMapper {
    AppliedLoanOffer toJson(LoanOfferRequest loanOfferRequest);
}