package com.abhi.payments.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MerchantProfileRequest(
        @NotBlank(message = "Business name cannot be blank")
        @Size(min = 3, max = 100, message = "Business name must be between 3 and 100 characters")
        String businessName
) {}