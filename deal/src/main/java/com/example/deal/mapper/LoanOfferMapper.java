package com.example.deal.mapper;

import com.example.credit.application.model.LoanOfferDTO;
import com.example.deal.entity.LoanOffer;
import org.mapstruct.Mapper;

@Mapper
public interface LoanOfferMapper {
    LoanOffer toEntity(LoanOfferDTO loanOfferDTO);

    LoanOfferDTO toDTO(LoanOffer loanOffer);
}