package com.abhi.payments.repository;

import com.abhi.payments.entity.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {
    Optional<ApiKey> findByPublishableKey(String publishableKey);
    List<ApiKey> findByMerchantProfileId(Long merchantId);
}