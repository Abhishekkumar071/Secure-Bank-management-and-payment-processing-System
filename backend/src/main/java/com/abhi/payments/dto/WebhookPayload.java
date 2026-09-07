package com.abhi.payments.dto;

import jakarta.validation.constraints.NotBlank;

public record WebhookPayload(
        @NotBlank String eventId,
        @NotBlank String eventType,
        @NotBlank String provider,
        @NotBlank String providerPaymentId,
        @NotBlank String orderId,
        String failureReason
) {}