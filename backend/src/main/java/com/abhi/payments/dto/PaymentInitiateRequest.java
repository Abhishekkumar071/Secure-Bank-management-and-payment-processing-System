package com.abhi.payments.dto;

import com.abhi.payments.common.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PaymentInitiateRequest(
        @NotBlank(message = "Order ID is required")
        String orderId,

        @NotNull(message = "Payment method is required")
        PaymentMethod method,

        String provider // Optional, defaults to MOCK if blank
) {}