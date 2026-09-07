package com.abhi.payments.controller;

import com.abhi.payments.dto.ApiResponse;
import com.abhi.payments.dto.WebhookPayload;
import com.abhi.payments.service.WebhookProcessingService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/webhooks")
public class WebhookController {

    private final WebhookProcessingService webhookProcessingService;
    private final ObjectMapper objectMapper;

    public WebhookController(WebhookProcessingService webhookProcessingService, ObjectMapper objectMapper) {
        this.webhookProcessingService = webhookProcessingService;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/gateway")
    public ResponseEntity<ApiResponse<String>> handleGatewayWebhook(
            @RequestHeader(value = "X-Webhook-Signature", required = false) String signature,
            @RequestBody String rawPayloadJson) {

        try {
            WebhookPayload payload = objectMapper.readValue(rawPayloadJson, WebhookPayload.class);
            webhookProcessingService.processWebhook(payload, rawPayloadJson, signature);
            return ResponseEntity.ok(new ApiResponse<>(true, "Webhook processed successfully", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}