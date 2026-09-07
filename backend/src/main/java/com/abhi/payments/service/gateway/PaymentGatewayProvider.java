package com.abhi.payments.service.gateway;

import com.abhi.payments.dto.GatewayOrderRequest;
import com.abhi.payments.dto.GatewayOrderResponse;

public interface PaymentGatewayProvider {

    String getProviderName();

    GatewayOrderResponse createGatewayOrder(GatewayOrderRequest request);

    boolean verifySignature(String orderId, String paymentId, String signature);
}