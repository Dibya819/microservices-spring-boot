package com.dibya.programming.kafkaconfig;

import com.dibya.programming.dtos.TrafficSignalStatusEvent;
import lombok.Getter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;


@Configuration
public class KafkaProducerConfig {


    @Bean
    public KafkaTemplate<String, TrafficSignalStatusEvent> kafkaTemplate(ProducerFactory<String, TrafficSignalStatusEvent> factory) {
        return new KafkaTemplate<>(factory);
    }

}
