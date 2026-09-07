package com.abhi.payments.service;

import com.abhi.payments.common.OrderStatus;
import com.abhi.payments.common.PaymentStatus;
import com.abhi.payments.dto.GatewayOrderRequest;
import com.abhi.payments.dto.GatewayOrderResponse;
import com.abhi.payments.dto.PaymentInitiateRequest;
import com.abhi.payments.dto.PaymentInitiateResponse;
import com.abhi.payments.entity.PaymentAttempt;
import com.abhi.payments.entity.PaymentOrder;
import com.abhi.payments.exception.CustomException;
import com.abhi.payments.repository.PaymentAttemptRepository;
import com.abhi.payments.repository.PaymentOrderRepository;
import com.abhi.payments.service.gateway.PaymentGatewayProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
public class PaymentExecutionService {

    private final PaymentOrderRepository orderRepository;
    private final PaymentAttemptRepository attemptRepository;
    private final Map<String, PaymentGatewayProvider> gatewayProviders;

    public PaymentExecutionService(PaymentOrderRepository orderRepository,
                                   PaymentAttemptRepository attemptRepository,
                                   Map<String, PaymentGatewayProvider> gatewayProviders) {
        this.orderRepository = orderRepository;
        this.attemptRepository = attemptRepository;
        this.gatewayProviders = gatewayProviders;
    }

    @Transactional
    public PaymentInitiateResponse initiatePayment(PaymentInitiateRequest request) {
        PaymentOrder order = orderRepository.findByOrderId(request.orderId())
                .orElseThrow(() -> new CustomException("Order not found with id: " + request.orderId()));

        if (order.getStatus() == OrderStatus.PAID) {
            throw new CustomException("Order is already paid. Cannot create a new payment attempt.");
        }

        String targetProvider = (request.provider() == null || request.provider().isBlank())
                ? "MOCK"
                : request.provider().toUpperCase();

        PaymentGatewayProvider provider = gatewayProviders.get(targetProvider);
        if (provider == null) {
            throw new CustomException("Unsupported payment provider: " + targetProvider);
        }

        GatewayOrderResponse gatewayResponse = provider.createGatewayOrder(
                new GatewayOrderRequest(order.getOrderId(), order.getAmount(), order.getCurrency(), order.getReceipt())
        );

        String internalPaymentId = "pay_" + UUID.randomUUID().toString().replace("-", "").substring(0, 14);

        PaymentAttempt attempt = new PaymentAttempt();
        attempt.setPaymentId(internalPaymentId);
        attempt.setPaymentOrder(order);
        attempt.setProvider(provider.getProviderName());
        attempt.setProviderPaymentId(gatewayResponse.providerOrderId());
        attempt.setMethod(request.method());
        attempt.setStatus(PaymentStatus.PROCESSING);
        attempt.setAmount(order.getAmount());

        attemptRepository.save(attempt);

        order.setStatus(OrderStatus.ATTEMPTED);
        orderRepository.save(order);

        return new PaymentInitiateResponse(
                attempt.getPaymentId(),
                order.getOrderId(),
                attempt.getAmount(),
                order.getCurrency(),
                attempt.getMethod(),
                attempt.getStatus(),
                attempt.getProvider(),
                attempt.getProviderPaymentId()
        );
    }
}