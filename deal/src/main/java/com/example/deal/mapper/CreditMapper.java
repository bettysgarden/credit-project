package com.example.deal.mapper;

import com.example.deal.model.dto.CreditResponse;
import com.example.deal.model.entity.CreditEntity;
import org.mapstruct.Mapper;

@Mapper
public interface CreditMapper {
    CreditEntity toEntity(CreditResponse response);
}
