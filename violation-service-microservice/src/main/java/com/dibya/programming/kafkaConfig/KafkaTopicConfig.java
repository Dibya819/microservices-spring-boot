package com.dibya.programming.kafkaConfig;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Value("${spring.kafka.topic.violation-notifications}")
    private String violationNotificationTopic;

    @Bean
    public NewTopic violationNotificationTopic() {
        return new NewTopic(violationNotificationTopic, 3, (short) 1);
    }
}
