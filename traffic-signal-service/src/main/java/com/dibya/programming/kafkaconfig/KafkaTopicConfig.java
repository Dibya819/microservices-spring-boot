package com.dibya.programming.kafkaconfig;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Value("${traffic.signal.topic}")
    private String trafficSignalTopic;

    @Bean
    public NewTopic trafficSignalTopic() {
        // 3 partitions, 1 replication factor (suitable for local dev)
        return new NewTopic(trafficSignalTopic, 3, (short) 1);
    }
}
