package com.abhi.payments.controller;

import com.abhi.payments.dto.ApiResponse;
import com.abhi.payments.dto.PaymentOrderRequest;
import com.abhi.payments.dto.PaymentOrderResponse;
import com.abhi.payments.service.PaymentOrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class PaymentOrderController {

    private final PaymentOrderService paymentOrderService;

    public PaymentOrderController(PaymentOrderService paymentOrderService) {
        this.paymentOrderService = paymentOrderService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PaymentOrderResponse>> createOrder(
            @Valid @RequestBody PaymentOrderRequest request,
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey, // Header mapping
            Authentication authentication) {

        String userEmail = authentication.getName();
        // Passing the key to the service layer
        PaymentOrderResponse response = paymentOrderService.createOrder(userEmail, request, idempotencyKey);

        return ResponseEntity.ok(new ApiResponse<>(true, "Order processed successfully", response));
    }
}