package com.abhi.payments.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "idempotency_records",
        // Composite Unique Constraint: Ek merchant same key dobara use nahi kar sakta
        uniqueConstraints = @UniqueConstraint(columnNames = {"merchant_id", "idempotency_key"})
)
public class IdempotencyRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "idempotency_key", nullable = false, length = 100)
    private String idempotencyKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id", nullable = false)
    private MerchantProfile merchantProfile;

    // We store the previously generated Order ID, so we can return it again
    @Column(nullable = false)
    private String responseOrderId;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() { this.createdAt = LocalDateTime.now(); }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getIdempotencyKey() { return idempotencyKey; }
    public void setIdempotencyKey(String idempotencyKey) { this.idempotencyKey = idempotencyKey; }
    public MerchantProfile getMerchantProfile() { return merchantProfile; }
    public void setMerchantProfile(MerchantProfile merchantProfile) { this.merchantProfile = merchantProfile; }
    public String getResponseOrderId() { return responseOrderId; }
    public void setResponseOrderId(String responseOrderId) { this.responseOrderId = responseOrderId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}