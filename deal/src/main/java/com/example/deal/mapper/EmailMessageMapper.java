package com.example.deal.mapper;

import com.example.deal.model.dto.EmailMessageDTO;
import com.example.deal.model.entity.EmailMessage;
import org.mapstruct.Mapper;

@Mapper
public interface EmailMessageMapper {
    EmailMessageDTO toDto(EmailMessage emailMessage);
}
