package ru.test.dossier.service;

import ru.test.dossier.model.dto.EmailMessageDTO;

public interface EmailService {

    void sendEmail(EmailMessageDTO emailMessage);
}
