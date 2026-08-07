package com.abhi.payments.service;

import com.abhi.payments.common.MerchantStatus;
import com.abhi.payments.dto.MerchantProfileRequest;
import com.abhi.payments.entity.MerchantProfile;
import com.abhi.payments.entity.User;
import com.abhi.payments.exception.CustomException;
import com.abhi.payments.repository.MerchantProfileRepository;
import com.abhi.payments.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MerchantService {

    private final MerchantProfileRepository merchantRepository;
    private final UserRepository userRepository;

    public MerchantService(MerchantProfileRepository merchantRepository, UserRepository userRepository) {
        this.merchantRepository = merchantRepository;
        this.userRepository = userRepository;
    }

    // @Transactional ensures atomicity: if DB save fails, entire transaction rolls back
    @Transactional
    public MerchantProfile createProfile(String userEmail, MerchantProfileRequest request) {
        if (merchantRepository.existsByUserEmail(userEmail)) {
            throw new CustomException("Merchant profile already exists for this user!");
        }

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new CustomException("User not found in system"));

        MerchantProfile profile = new MerchantProfile();
        profile.setBusinessName(request.businessName());
        profile.setUser(user);
        profile.setStatus(MerchantStatus.PENDING); // Hardcoding to PENDING to avoid client manipulating it

        return merchantRepository.save(profile);
    }
}