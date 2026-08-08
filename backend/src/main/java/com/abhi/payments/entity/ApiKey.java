package com.abhi.payments.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "api_keys")
public class ApiKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String publishableKey;

    @Column(nullable = false)
    private String secretKeyHash; // We NEVER store the plain secret key

    @Column(nullable = false)
    private String environment; // "TEST" or "LIVE"

    @Column(nullable = false)
    private boolean isActive = true;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    // Many API Keys can belong to One Merchant
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id", nullable = false)
    private MerchantProfile merchantProfile;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPublishableKey() { return publishableKey; }
    public void setPublishableKey(String publishableKey) { this.publishableKey = publishableKey; }

    public String getSecretKeyHash() { return secretKeyHash; }
    public void setSecretKeyHash(String secretKeyHash) { this.secretKeyHash = secretKeyHash; }

    public String getEnvironment() { return environment; }
    public void setEnvironment(String environment) { this.environment = environment; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public MerchantProfile getMerchantProfile() { return merchantProfile; }
    public void setMerchantProfile(MerchantProfile merchantProfile) { this.merchantProfile = merchantProfile; }
}