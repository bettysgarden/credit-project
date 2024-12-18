package ru.test.dossier.service.implementation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.test.dossier.model.dto.EmailMessageDTO;
import ru.test.dossier.service.EmailService;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    @Override
    public void sendEmail(EmailMessageDTO emailMessage) {

        log.warn("LIZAAAAAAA LIZAAAAAAA LIZAAAAAAA LIZAAAAAAA : we are at sending email stage");
        log.warn(emailMessage.toString());
    }
}
