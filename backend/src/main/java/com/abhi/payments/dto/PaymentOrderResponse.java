package com.abhi.payments.dto;

public record PaymentOrderResponse(
        String orderId,
        Long amount,
        String currency,
        String status,
        String receipt
) {}