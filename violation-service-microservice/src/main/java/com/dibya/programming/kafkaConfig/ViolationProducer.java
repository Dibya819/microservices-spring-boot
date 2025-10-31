package com.dibya.programming.kafkaConfig;

import com.dibya.programming.dtos.ViolationNotificationEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ViolationProducer {

    private final KafkaTemplate<String, ViolationNotificationEvent> kafkaTemplate;

    @Value("${spring.kafka.topic.violation-notifications}")
    private String topic;

    public ViolationProducer(KafkaTemplate<String, ViolationNotificationEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendViolationEvent(ViolationNotificationEvent event) {
        kafkaTemplate.send(topic, event);
        System.out.println("📤 Sent Violation Notification to Kafka topic [" + topic + "]: " + event);
    }
}
