package com.dibya.programming.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // External user id from User Service (driver)
    @Column(nullable = false)
    private Long userId;

    // Link to specific violation if paying a single violation
    @Column(nullable = true)
    private Long violationId;

    @Column(nullable = false)
    private Double amount;

    // Razorpay order id (created when initiating an order)
    @Column(length = 100)
    private String orderId;

    // Razorpay payment id (populated after webhook/confirmation)
    @Column(length = 100)
    private String paymentId;

    // Gateway capture/checkout id or transaction id (if returned)
    @Column(length = 100)
    private String gatewayTxnId;

    // Payment method used (CARD, UPI, NETBANKING, WALLET, etc)
    @Column(length = 50)
    private String paymentMethod;

    // Status: CREATED, PENDING, SUCCESS, FAILED, REFUNDED
    @Column(length = 30, nullable = false)
    private String status;

    // When the payment record was created
    @Column(nullable = false)
    private LocalDateTime createdAt;

    // When the payment was completed/updated
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) this.status = "CREATED";
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
