package com.abhi.payments.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PaymentOrderRequest(
        @NotNull(message = "Amount is required")
        @Min(value = 100, message = "Minimum amount must be 1.00 (100 paise)")
        Long amount,

        @NotBlank(message = "Currency is required")
        String currency,

        String receipt,
        String customerName,
        String customerEmail,
        String customerPhone
) {}