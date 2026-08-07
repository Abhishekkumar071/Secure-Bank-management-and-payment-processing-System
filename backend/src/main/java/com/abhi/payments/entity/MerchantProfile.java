package com.abhi.payments.entity;

import com.abhi.payments.common.MerchantStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "merchant_profiles")
public class MerchantProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String businessName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MerchantStatus status;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    // Relational Mapping: One User has One MerchantProfile
    // FetchType.LAZY ensures User data is loaded only when explicitly accessed
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, unique = true)
    @JsonIgnore // Prevent infinite recursion during JSON serialization
    private User user;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = MerchantStatus.PENDING; // Default status
        }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getBusinessName() { return businessName; }
    public void setBusinessName(String businessName) { this.businessName = businessName; }

    public MerchantStatus getStatus() { return status; }
    public void setStatus(MerchantStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}