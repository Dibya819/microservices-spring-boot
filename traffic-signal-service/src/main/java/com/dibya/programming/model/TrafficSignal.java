package com.dibya.programming.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "traffic_signals", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"location_area"})
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrafficSignal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   @Embedded
    private Location location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SignalStatus status;

    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;

    @Column(name = "manual_override", nullable = false)
    private Boolean manualOverride = false;

    @Column(columnDefinition = "TEXT")
    private String metadata;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        lastUpdated = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        lastUpdated = LocalDateTime.now();
    }

    public enum SignalStatus {
        RED, GREEN, YELLOW
    }
}
