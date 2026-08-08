package com.abhi.payments.controller;

import com.abhi.payments.dto.ApiKeyResponse;
import com.abhi.payments.dto.ApiResponse;
import com.abhi.payments.service.ApiKeyService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/merchants/api-keys")
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    public ApiKeyController(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    // Generate New API Keys - Protected Route
    @PostMapping("/generate")
    public ResponseEntity<ApiResponse<ApiKeyResponse>> generateKeys(
            @RequestParam(defaultValue = "TEST") String environment,
            Authentication authentication) {

        String userEmail = authentication.getName();
        ApiKeyResponse keyResponse = apiKeyService.generateApiKeys(userEmail, environment);

        return ResponseEntity.ok(new ApiResponse<>(true, "API Keys generated successfully", keyResponse));
    }
}