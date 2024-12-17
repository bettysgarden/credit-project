package com.example.deal.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Slf4j
@RequiredArgsConstructor
@Component
public class Publisher {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void send(String topicName, String payload) {
        kafkaTemplate.send(topicName, payload)
                .exceptionally(ex -> {
                            log.error("Error. Unable to send message for {} due to: {}",
                                    topicName,
                                    ex.getMessage());
                            return null;
                        }
                );
    }
}