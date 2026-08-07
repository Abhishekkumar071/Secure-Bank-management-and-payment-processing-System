package com.abhi.payments.repository;

import com.abhi.payments.entity.MerchantProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MerchantProfileRepository extends JpaRepository<MerchantProfile, Long> {
    Optional<MerchantProfile> findByUserEmail(String email);
    boolean existsByUserEmail(String email);
}