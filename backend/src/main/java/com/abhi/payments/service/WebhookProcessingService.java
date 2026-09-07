package com.abhi.payments.service;

import com.abhi.payments.common.OrderStatus;
import com.abhi.payments.common.PaymentStatus;
import com.abhi.payments.dto.WebhookPayload;
import com.abhi.payments.entity.PaymentAttempt;
import com.abhi.payments.entity.PaymentOrder;
import com.abhi.payments.entity.WebhookEvent;
import com.abhi.payments.exception.CustomException;
import com.abhi.payments.repository.PaymentAttemptRepository;
import com.abhi.payments.repository.PaymentOrderRepository;
import com.abhi.payments.repository.WebhookEventRepository;
import com.abhi.payments.service.gateway.PaymentGatewayProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.util.Map;

@Service
public class WebhookProcessingService {

    private final WebhookEventRepository webhookEventRepository;
    private final PaymentOrderRepository orderRepository;
    private final PaymentAttemptRepository attemptRepository;
    private final Map<String, PaymentGatewayProvider> gatewayProviders;

    public WebhookProcessingService(WebhookEventRepository webhookEventRepository,
                                    PaymentOrderRepository orderRepository,
                                    PaymentAttemptRepository attemptRepository,
                                    Map<String, PaymentGatewayProvider> gatewayProviders) {
        this.webhookEventRepository = webhookEventRepository;
        this.orderRepository = orderRepository;
        this.attemptRepository = attemptRepository;
        this.gatewayProviders = gatewayProviders;
    }

    @Transactional
    public void processWebhook(WebhookPayload payload, String rawBody, String signature) {
        // 1. Idempotency check: Ignore already processed events
        if (webhookEventRepository.existsByEventId(payload.eventId())) {
            return;
        }

        // 2. Cryptographic signature check via provider strategy
        PaymentGatewayProvider provider = gatewayProviders.get(payload.provider().toUpperCase());
        if (provider == null || !provider.verifySignature(payload.orderId(), payload.providerPaymentId(), signature)) {
            throw new CustomException("Invalid webhook signature verification failed");
        }

        // 3. Log the raw webhook event for compliance and replay safety
        WebhookEvent event = new WebhookEvent();
        event.setEventId(payload.eventId());
        event.setProvider(payload.provider().toUpperCase());
        event.setEventType(payload.eventType());
        event.setPayload(rawBody);
        event.setProcessed(true);
        webhookEventRepository.save(event);

        // 4. Update Payment Attempt & Order atomically
        PaymentOrder order = orderRepository.findByOrderId(payload.orderId())
                .orElseThrow(() -> new CustomException("Order associated with webhook not found: " + payload.orderId()));

        PaymentAttempt attempt = attemptRepository.findByProviderPaymentId(payload.providerPaymentId())
                .orElseThrow(() -> new CustomException("Payment attempt not found: " + payload.providerPaymentId()));

        if ("payment.captured".equalsIgnoreCase(payload.eventType())) {
            attempt.setStatus(PaymentStatus.SUCCESS);
            order.setStatus(OrderStatus.PAID);
        } else if ("payment.failed".equalsIgnoreCase(payload.eventType())) {
            attempt.setStatus(PaymentStatus.FAILED);
            attempt.setFailureReason(payload.failureReason() != null ? payload.failureReason() : "Payment rejected by bank");
            order.setStatus(OrderStatus.FAILED);
        }

        attemptRepository.save(attempt);
        orderRepository.save(order);
    }
}