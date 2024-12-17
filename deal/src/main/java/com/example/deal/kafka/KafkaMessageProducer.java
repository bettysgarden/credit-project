package com.example.deal.kafka;

import com.example.deal.model.dto.EmailMessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaMessageProducer {

    private final KafkaTemplate<String, EmailMessageDTO> kafkaTemplate;

    public void sendMessage(String topic, EmailMessageDTO message) {
        kafkaTemplate.send(topic, message)
                .exceptionally(ex -> {
                            log.error("Error. Unable to send message for {} due to: {}",
                                    topic,
                                    ex.getMessage());
                            return null;
                        }
                );
    }
}