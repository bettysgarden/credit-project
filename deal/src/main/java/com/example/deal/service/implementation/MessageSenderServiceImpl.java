package com.example.deal.service.implementation;

import com.example.deal.kafka.KafkaMessageProducer;
import com.example.deal.mapper.EmailMessageMapper;
import com.example.deal.model.entity.EmailMessage;
import com.example.deal.service.MessageSenderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MessageSenderServiceImpl implements MessageSenderService {

    private final KafkaMessageProducer messageProducer;
    private final EmailMessageMapper mapper;

    @Value("${spring.kafka.topic.finish-registration}")
    private String finishRegistrationTopic;
    @Value("${spring.kafka.topic.create-documents}")
    private String createDocumentsTopic;
    @Value("${spring.kafka.topic.send-documents}")
    private String sendDocumentsTopic;
    @Value("${spring.kafka.topic.send-ses}")
    private String sendSesTopic;
    @Value("${spring.kafka.topic.credit-issued}")
    private String creditIssuedTopic;
    @Value("${spring.kafka.topic.application-denied}")
    private String applicationDeniedTopic;

    @Override
    public void sendClientEmail(EmailMessage emailMessage) {
        send(finishRegistrationTopic, emailMessage);
    }

    private void send(String topic, EmailMessage emailMessage) {
        messageProducer.sendMessage(topic, mapper.toDto(emailMessage));
    }
}
