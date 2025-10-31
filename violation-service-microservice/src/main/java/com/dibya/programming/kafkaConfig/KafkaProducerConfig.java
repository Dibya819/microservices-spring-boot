package com.dibya.programming.kafkaConfig;

import com.dibya.programming.dtos.ViolationNotificationEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public KafkaTemplate<String, ViolationNotificationEvent> kafkaTemplate(
            ProducerFactory<String, ViolationNotificationEvent> factory) {
        return new KafkaTemplate<>(factory);
    }
}
