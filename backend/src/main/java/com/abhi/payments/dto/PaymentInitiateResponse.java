package com.abhi.payments.dto;

import com.abhi.payments.common.PaymentMethod;
import com.abhi.payments.common.PaymentStatus;

public record PaymentInitiateResponse(
        String paymentId,
        String orderId,
        Long amount,
        String currency,
        PaymentMethod method,
        PaymentStatus status,
        String provider,
        String providerOrderId
) {}