package com.abhi.payments.common;

public enum MerchantStatus {
    PENDING,    // Just registered, KYC pending
    ACTIVE,     // Verified, can process payments
    SUSPENDED   // Blocked due to fraud or violation
}