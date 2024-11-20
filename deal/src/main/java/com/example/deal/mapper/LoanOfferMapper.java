package com.example.deal.mapper;

import com.example.deal.model.LoanOffer;
import com.example.deal.model.dto.LoanOfferRequest;
import com.example.deal.model.entity.jsonb.AppliedLoanOffer;
import org.mapstruct.Mapper;

@Mapper
public interface LoanOfferMapper {
    LoanOffer toEntity(LoanOfferRequest loanOfferDTO);

    LoanOfferRequest toDTO(LoanOffer loanOffer);

    AppliedLoanOffer toJson(LoanOfferRequest loanOfferRequest);
}