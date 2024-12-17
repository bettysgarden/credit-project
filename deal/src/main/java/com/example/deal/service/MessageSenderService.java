package com.example.deal.service;

import com.example.deal.model.entity.EmailMessage;

public interface MessageSenderService {
    void sendClientEmail(EmailMessage emailMessage);
}
