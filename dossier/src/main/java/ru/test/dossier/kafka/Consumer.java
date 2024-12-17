package ru.test.dossier.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class Consumer {

    private static final String finishRegistrationTopic = "${spring.kafka.topic.finish-registration}";
    private static final String createDocumentsTopic = "${spring.kafka.topic.create-documents}";
    private static final String sendDocumentsTopic = "${spring.kafka.topic.send-documents}";
    private static final String sendSesTopic = "${spring.kafka.consumer.send-ses}";
    private static final String creditIssuedTopic = "${spring.kafka.consumer.credit-issued}";
    private static final String applicationDeniedTopic = "${spring.kafka.consumer.application-denied}";
    private static final String kafkaConsumerGroupId = "${spring.kafka.consumer.group-id}";


}

