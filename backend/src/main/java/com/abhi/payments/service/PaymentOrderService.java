package com.abhi.payments.service;

import com.abhi.payments.common.OrderStatus;
import com.abhi.payments.dto.PaymentOrderRequest;
import com.abhi.payments.dto.PaymentOrderResponse;
import com.abhi.payments.entity.Customer;
import com.abhi.payments.entity.MerchantProfile;
import com.abhi.payments.entity.PaymentOrder;
import com.abhi.payments.exception.CustomException;
import com.abhi.payments.repository.CustomerRepository;
import com.abhi.payments.repository.MerchantProfileRepository;
import com.abhi.payments.repository.PaymentOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PaymentOrderService {

    private final PaymentOrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final MerchantProfileRepository merchantProfileRepository;

    public PaymentOrderService(PaymentOrderRepository orderRepository,
                               CustomerRepository customerRepository,
                               MerchantProfileRepository merchantProfileRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.merchantProfileRepository = merchantProfileRepository;
    }

    @Transactional
    public PaymentOrderResponse createOrder(String userEmail, PaymentOrderRequest request) {
        MerchantProfile profile = merchantProfileRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new CustomException("Merchant profile not found"));

        // 1. Handle Customer
        Customer customer = new Customer();
        customer.setName(request.customerName());
        customer.setEmail(request.customerEmail());
        customer.setPhone(request.customerPhone());
        customer.setMerchantProfile(profile);
        customerRepository.save(customer);

        // 2. Generate Unique Order ID (O(1) time)
        String uniqueOrderId = "order_" + UUID.randomUUID().toString().replace("-", "").substring(0, 14);

        // 3. Create Order
        PaymentOrder order = new PaymentOrder();
        order.setOrderId(uniqueOrderId);
        order.setAmount(request.amount());
        order.setCurrency(request.currency().toUpperCase());
        order.setStatus(OrderStatus.CREATED);
        order.setReceipt(request.receipt());
        order.setMerchantProfile(profile);
        order.setCustomer(customer);

        orderRepository.save(order);

        return new PaymentOrderResponse(
                order.getOrderId(),
                order.getAmount(),
                order.getCurrency(),
                order.getStatus().name(),
                order.getReceipt()
        );
    }
}