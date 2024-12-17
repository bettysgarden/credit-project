package ru.test.dossier.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.test.dossier.model.dto.EmailMessageDTO;
import ru.test.dossier.service.EmailService;

@RequiredArgsConstructor
@Component
public class KafkaMessageListener {
    private final EmailService emailService;

    private static final String finishRegistrationTopic = "${spring.kafka.topic.finish-registration}";
    private static final String createDocumentsTopic = "${spring.kafka.topic.create-documents}";
    private static final String sendDocumentsTopic = "${spring.kafka.topic.send-documents}";
    private static final String sendSesTopic = "${spring.kafka.topic.send-ses}";
    private static final String creditIssuedTopic = "${spring.kafka.topic.credit-issued}";
    private static final String applicationDeniedTopic = "${spring.kafka.topic.application-denied}";
    private static final String kafkaConsumerGroupId = "${spring.kafka.consumer.group-id}";

    @KafkaListener(groupId = kafkaConsumerGroupId, topics = finishRegistrationTopic)
    public void processFinishRegistration(EmailMessageDTO emailMessage) {
        emailService.sendEmail(emailMessage);
    }

    @KafkaListener(groupId = kafkaConsumerGroupId, topics = createDocumentsTopic)
    public void processCreatingDocuments(EmailMessageDTO emailMessage) {
        emailService.sendEmail(emailMessage);
    }

    @KafkaListener(groupId = kafkaConsumerGroupId, topics = sendDocumentsTopic)
    public void processSendingDocuments(EmailMessageDTO emailMessage) {
        emailService.sendEmail(emailMessage);
    }

    @KafkaListener(groupId = kafkaConsumerGroupId, topics = sendSesTopic)
    public void processSendingSes(EmailMessageDTO emailMessage) {
        emailService.sendEmail(emailMessage);
    }

    @KafkaListener(groupId = kafkaConsumerGroupId, topics = creditIssuedTopic)
    public void processCreditIssuing(EmailMessageDTO emailMessage) {
        emailService.sendEmail(emailMessage);
    }

    @KafkaListener(groupId = kafkaConsumerGroupId, topics = applicationDeniedTopic)
    public void processApplicationDenial(EmailMessageDTO emailMessage) {
        emailService.sendEmail(emailMessage);
    }

}

