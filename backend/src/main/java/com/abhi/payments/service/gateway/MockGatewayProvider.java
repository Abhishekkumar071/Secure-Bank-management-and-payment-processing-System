package com.abhi.payments.service.gateway;

import com.abhi.payments.dto.GatewayOrderRequest;
import com.abhi.payments.dto.GatewayOrderResponse;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("MOCK")
public class MockGatewayProvider implements PaymentGatewayProvider {

    @Override
    public String getProviderName() {
        return "MOCK";
    }

    @Override
    public GatewayOrderResponse createGatewayOrder(GatewayOrderRequest request) {
        String mockProviderOrderId = "mock_order_" + UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        return new GatewayOrderResponse(mockProviderOrderId, getProviderName(), "CREATED");
    }

    @Override
    public boolean verifySignature(String orderId, String paymentId, String signature) {
        // Mock gateway signature verification logic
        return signature != null && signature.startsWith("sig_mock_");
    }
}