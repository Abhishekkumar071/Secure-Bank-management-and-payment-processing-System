package com.abhi.payments.repository;

import com.abhi.payments.entity.PaymentAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface PaymentAttemptRepository extends JpaRepository<PaymentAttempt, Long> {
    Optional<PaymentAttempt> findByPaymentId(String paymentId);
    Optional<PaymentAttempt> findByProviderPaymentId(String providerPaymentId);
    List<PaymentAttempt> findByPaymentOrderId(Long orderId);
}