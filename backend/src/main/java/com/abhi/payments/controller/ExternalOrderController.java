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
@RequestMapping("/api/external/orders") // Notice the 'external' path
public class ExternalOrderController {

    private final PaymentOrderService paymentOrderService;

    public ExternalOrderController(PaymentOrderService paymentOrderService) {
        this.paymentOrderService = paymentOrderService;
    }

    // Creates a new payment order using M2M API Keys
    @PostMapping
    public ResponseEntity<ApiResponse<PaymentOrderResponse>> createOrderExternal(
            @Valid @RequestBody PaymentOrderRequest request,
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey,
            Authentication authentication) {

        // Identity has been verified by ApiKeyFilter and set in context
        String userEmail = authentication.getName();

        PaymentOrderResponse response = paymentOrderService.createOrder(userEmail, request, idempotencyKey);

        return ResponseEntity.ok(new ApiResponse<>(true, "Order created via API Keys", response));
    }
}