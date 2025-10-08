package com.dibya.programming.scheduler;

import com.dibya.programming.dtos.TrafficSignalStatusEvent;
import com.dibya.programming.kafkaconfig.KafkaProducerConfig;
import com.dibya.programming.model.TrafficSignal;
import com.dibya.programming.repository.SignalServiceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TrafficSignalScheduler {

    private final SignalServiceRepo serviceRepo;
    private final KafkaTemplate<String, TrafficSignalStatusEvent> kafkaTemplate;
    private final KafkaProducerConfig producerConfig;

    @Value("${traffic.signal.topic}")
    private String topic;


    @Autowired
    public TrafficSignalScheduler(SignalServiceRepo serviceRepo, KafkaTemplate<String, TrafficSignalStatusEvent> kafkaTemplate, KafkaProducerConfig producerConfig) {
        this.serviceRepo = serviceRepo;
        this.kafkaTemplate = kafkaTemplate;
        this.producerConfig = producerConfig;
    }

    @Scheduled(fixedRate = 30000)
    public void updateTrafficSignals() {
        List<TrafficSignal> signals = serviceRepo.findAll();

        for (TrafficSignal signal : signals) {

            if (Boolean.TRUE.equals(signal.getManualOverride())) continue;

            TrafficSignal.SignalStatus newStatus = getNextStatus(signal.getStatus());
            signal.setStatus(newStatus);

            serviceRepo.save(signal);

            TrafficSignalStatusEvent event = TrafficSignalStatusEvent.builder()
                    .area(signal.getLocation().getArea())
                    .city(signal.getLocation().getCity())
                    .status(newStatus.name())
                    .timestamp(signal.getLastUpdated())
                    .build();

            kafkaTemplate.send(topic, event);
        }
    }

    private TrafficSignal.SignalStatus getNextStatus(TrafficSignal.SignalStatus current) {
        return switch (current) {
            case RED -> TrafficSignal.SignalStatus.GREEN;
            case GREEN -> TrafficSignal.SignalStatus.YELLOW;
            case YELLOW -> TrafficSignal.SignalStatus.RED;
        };
    }
}
