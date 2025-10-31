package com.dibya.programming.listner;

import com.dibya.programming.dtos.ViolationNotificationEvent;
import com.dibya.programming.service.EmailService;
import com.dibya.programming.service.NotificationPersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ViolationListener {

    private final EmailService emailService;
    private final NotificationPersistenceService persistenceService;


    @Autowired
    public ViolationListener(EmailService emailService, NotificationPersistenceService persistenceService) {
        this.emailService = emailService;
        this.persistenceService = persistenceService;
    }

    @KafkaListener(
            topics = "${spring.kafka.topic.violation-notifications}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(ViolationNotificationEvent event) {
        System.out.println("📩 Received Violation Event: " + event);

        String subject = "Traffic Violation Notice: " + event.getViolationType();
        String body = String.format(
                "Dear %s,\n\n" +
                "Your vehicle %s (%s) was recorded for %s on %s.\n" +
                "Fine Amount: Rs. %.2f\n" +
                "Location: %s\n\n" +
                "Please clear your fine at the earliest.\n\n" +
                "Regards,\nCity Traffic Department",
                event.getUserName(),
                event.getVehicleRegistrationNumber(),
                event.getVehicleType(),
                event.getViolationType(),
                event.getTimestamp(),
                event.getFineAmount(),
                event.getLocation()
        );

        boolean emailSent = emailService.sendEmail(event.getUserEmail(), subject, body);
        persistenceService.saveNotification(event, emailSent);

        if (emailSent)
            System.out.println("✅ Email sent successfully to " + event.getUserEmail());
        else
            System.err.println("❌ Failed to send email to " + event.getUserEmail());
    }
}
