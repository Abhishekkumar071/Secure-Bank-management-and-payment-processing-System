package com.abhi.payments.controller;

import com.abhi.payments.dto.ApiResponse;
import com.abhi.payments.exception.CustomException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<Map<String, String>>> healthCheck() {
        Map<String, String> data = new HashMap<>();
        data.put("status", "UP");
        data.put("module", "Payment Orchestration API");

        ApiResponse<Map<String, String>> response = new ApiResponse<>(true, "System is healthy", data);
        return ResponseEntity.ok(response);
    }

    // Testing our Global Exception Handler
    @GetMapping("/test-error")
    public ResponseEntity<ApiResponse<String>> testError() {
        throw new CustomException("This is a test error to check global exception handling!");
    }
}