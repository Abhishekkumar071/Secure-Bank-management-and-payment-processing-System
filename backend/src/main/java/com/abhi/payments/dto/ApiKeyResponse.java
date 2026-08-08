package com.abhi.payments.dto;

public record ApiKeyResponse(
        String publishableKey,
        String secretKey,    // Only shown once during creation!
        String environment,
        String message
) {}