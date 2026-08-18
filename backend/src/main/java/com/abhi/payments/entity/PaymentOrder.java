package com.abhi.payments.entity;

import com.abhi.payments.common.OrderStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment_orders")
public class PaymentOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String orderId; // e.g. order_8f7d9a...

    @Column(nullable = false)
    private Long amount; // In smallest unit (paise). Example: 50000 = ₹500.00

    @Column(nullable = false, length = 3)
    private String currency; // "INR", "USD"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    private String receipt; // Merchant's internal reference ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id", nullable = false)
    private MerchantProfile merchantProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() { this.createdAt = LocalDateTime.now(); }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public Long getAmount() { return amount; }
    public void setAmount(Long amount) { this.amount = amount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public String getReceipt() { return receipt; }
    public void setReceipt(String receipt) { this.receipt = receipt; }
    public MerchantProfile getMerchantProfile() { return merchantProfile; }
    public void setMerchantProfile(MerchantProfile merchantProfile) { this.merchantProfile = merchantProfile; }
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
}