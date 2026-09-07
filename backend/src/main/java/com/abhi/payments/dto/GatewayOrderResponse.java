package com.abhi.payments.dto;

public record GatewayOrderResponse(
        String providerOrderId,
        String providerName,
        String status
) {}