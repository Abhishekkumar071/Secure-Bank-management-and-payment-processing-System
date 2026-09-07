package com.abhi.payments.controller;

import com.abhi.payments.dto.ApiResponse;
import com.abhi.payments.dto.PaymentInitiateRequest;
import com.abhi.payments.dto.PaymentInitiateResponse;
import com.abhi.payments.service.PaymentExecutionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentExecutionService paymentExecutionService;

    public PaymentController(PaymentExecutionService paymentExecutionService) {
        this.paymentExecutionService = paymentExecutionService;
    }

    @PostMapping("/pay")
    public ResponseEntity<ApiResponse<PaymentInitiateResponse>> pay(@Valid @RequestBody PaymentInitiateRequest request) {
        PaymentInitiateResponse response = paymentExecutionService.initiatePayment(request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Payment attempt initiated", response));
    }
}