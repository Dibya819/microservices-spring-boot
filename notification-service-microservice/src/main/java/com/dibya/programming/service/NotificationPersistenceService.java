package com.dibya.programming.service;

import com.dibya.programming.dtos.ViolationNotificationEvent;
import com.dibya.programming.model.NotificationEntity;
import com.dibya.programming.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationPersistenceService {

    private final NotificationRepository repository;

    public NotificationPersistenceService(NotificationRepository repository) {
        this.repository = repository;
    }

    public void saveNotification(ViolationNotificationEvent event, boolean emailSent) {
        NotificationEntity entity = NotificationEntity.builder()
                .userName(event.getUserName())
                .userEmail(event.getUserEmail())
                .violationType(event.getViolationType())
                .vehicleRegistrationNumber(event.getVehicleRegistrationNumber())
                .vehicleType(event.getVehicleType())
                .fineAmount(event.getFineAmount())
                .location(event.getLocation())
                .timestamp(event.getTimestamp())
                .emailSent(emailSent)
                .createdAt(LocalDateTime.now())
                .build();

        repository.save(entity);
        System.out.println("💾 Notification saved to DB for user: " + event.getUserName());
    }
}
