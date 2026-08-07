package com.abhi.payments.controller;

import com.abhi.payments.dto.ApiResponse;
import com.abhi.payments.dto.MerchantProfileRequest;
import com.abhi.payments.entity.MerchantProfile;
import com.abhi.payments.service.MerchantService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/merchants")
public class MerchantController {

    private final MerchantService merchantService;

    public MerchantController(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    // Creating Profile - Protected Route (Requires MERCHANT role)
    @PostMapping("/profile")
    public ResponseEntity<ApiResponse<MerchantProfile>> createProfile(
            @Valid @RequestBody MerchantProfileRequest request,
            Authentication authentication) {

        // Extracting user identity secured by JWT validation
        String userEmail = authentication.getName();

        MerchantProfile profile = merchantService.createProfile(userEmail, request);

        return ResponseEntity.ok(new ApiResponse<>(true, "Merchant profile created successfully", profile));
    }
}