package com.abhi.payments.repository;

import com.abhi.payments.entity.IdempotencyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface IdempotencyRecordRepository extends JpaRepository<IdempotencyRecord, Long> {

    // Fast lookup using the composite index we defined in the Entity
    Optional<IdempotencyRecord> findByMerchantProfileIdAndIdempotencyKey(Long merchantId, String key);
}