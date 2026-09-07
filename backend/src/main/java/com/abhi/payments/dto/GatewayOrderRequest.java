package com.abhi.payments.dto;

public record GatewayOrderRequest(
        String orderId,
        Long amount,
        String currency,
        String receipt
) {}