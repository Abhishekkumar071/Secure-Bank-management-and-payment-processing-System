package com.abhi.payments.service;

import com.abhi.payments.common.OrderStatus;
import com.abhi.payments.dto.PaymentOrderRequest;
import com.abhi.payments.dto.PaymentOrderResponse;
import com.abhi.payments.entity.Customer;
import com.abhi.payments.entity.IdempotencyRecord;
import com.abhi.payments.entity.MerchantProfile;
import com.abhi.payments.entity.PaymentOrder;
import com.abhi.payments.exception.CustomException;
import com.abhi.payments.repository.CustomerRepository;
import com.abhi.payments.repository.IdempotencyRecordRepository;
import com.abhi.payments.repository.MerchantProfileRepository;
import com.abhi.payments.repository.PaymentOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentOrderService {

    private final PaymentOrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final MerchantProfileRepository merchantProfileRepository;
    private final IdempotencyRecordRepository idempotencyRecordRepository;

    public PaymentOrderService(PaymentOrderRepository orderRepository,
                               CustomerRepository customerRepository,
                               MerchantProfileRepository merchantProfileRepository,
                               IdempotencyRecordRepository idempotencyRecordRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.merchantProfileRepository = merchantProfileRepository;
        this.idempotencyRecordRepository = idempotencyRecordRepository;
    }

    @Transactional
    public PaymentOrderResponse createOrder(String userEmail, PaymentOrderRequest request, String idempotencyKey) {
        MerchantProfile profile = merchantProfileRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new CustomException("Merchant profile not found"));

        // === IDEMPOTENCY CHECK ===
        // 1. Agar key bheji gayi hai, toh database me check karo kya ye key is merchant ne pehle use ki hai?
        if (idempotencyKey != null && !idempotencyKey.trim().isEmpty()) {
            Optional<IdempotencyRecord> existingRecord = idempotencyRecordRepository
                    .findByMerchantProfileIdAndIdempotencyKey(profile.getId(), idempotencyKey);

            // 2. Agar record mil gaya, iska matlab client ne retry kiya hai.
            // Naya order banane ki jagah purana order fetch karke return kar do!
            if (existingRecord.isPresent()) {
                String oldOrderId = existingRecord.get().getResponseOrderId();
                PaymentOrder oldOrder = orderRepository.findByOrderId(oldOrderId)
                        .orElseThrow(() -> new CustomException("Order mapped to key not found"));

                return new PaymentOrderResponse(
                        oldOrder.getOrderId(), oldOrder.getAmount(),
                        oldOrder.getCurrency(), oldOrder.getStatus().name(), oldOrder.getReceipt()
                );
            }
        }

        // === NORMAL ORDER CREATION (Agar key nayi hai) ===
        Customer customer = new Customer();
        customer.setName(request.customerName());
        customer.setEmail(request.customerEmail());
        customer.setPhone(request.customerPhone());
        customer.setMerchantProfile(profile);
        customerRepository.save(customer);

        String uniqueOrderId = "order_" + UUID.randomUUID().toString().replace("-", "").substring(0, 14);

        PaymentOrder order = new PaymentOrder();
        order.setOrderId(uniqueOrderId);
        order.setAmount(request.amount());
        order.setCurrency(request.currency().toUpperCase());
        order.setStatus(OrderStatus.CREATED);
        order.setReceipt(request.receipt());
        order.setMerchantProfile(profile);
        order.setCustomer(customer);

        orderRepository.save(order);

        // === SAVE IDEMPOTENCY RECORD ===
        // Naya order banne ke baad, hum is key ko DB me save kar lete hain taaki dobara na use ho paye.
        if (idempotencyKey != null && !idempotencyKey.trim().isEmpty()) {
            IdempotencyRecord record = new IdempotencyRecord();
            record.setIdempotencyKey(idempotencyKey);
            record.setMerchantProfile(profile);
            record.setResponseOrderId(uniqueOrderId);
            idempotencyRecordRepository.save(record);
        }

        return new PaymentOrderResponse(
                order.getOrderId(), order.getAmount(), order.getCurrency(),
                order.getStatus().name(), order.getReceipt()
        );
    }
}