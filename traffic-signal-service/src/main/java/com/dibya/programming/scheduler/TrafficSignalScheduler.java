package com.dibya.programming.scheduler;

import com.dibya.programming.model.TrafficSignal;
import com.dibya.programming.repository.SignalServiceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TrafficSignalScheduler {

    private final SignalServiceRepo serviceRepo;

    @Autowired
    public TrafficSignalScheduler(SignalServiceRepo serviceRepo) {
        this.serviceRepo = serviceRepo;
    }

    @Scheduled(fixedRate = 30000)
    public void updateTrafficSignals() {
        List<TrafficSignal> signals = serviceRepo.findAll();

        for (TrafficSignal signal : signals) {

            if (Boolean.TRUE.equals(signal.getManualOverride())) continue;

            TrafficSignal.SignalStatus newStatus = getNextStatus(signal.getStatus());
            signal.setStatus(newStatus);

            serviceRepo.save(signal);
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
