package com.abhi.payments.service;

import com.abhi.payments.dto.ApiKeyResponse;
import com.abhi.payments.entity.ApiKey;
import com.abhi.payments.entity.MerchantProfile;
import com.abhi.payments.exception.CustomException;
import com.abhi.payments.repository.ApiKeyRepository;
import com.abhi.payments.repository.MerchantProfileRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;
    private final MerchantProfileRepository merchantProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecureRandom secureRandom = new SecureRandom();

    public ApiKeyService(ApiKeyRepository apiKeyRepository,
                         MerchantProfileRepository merchantProfileRepository,
                         PasswordEncoder passwordEncoder) {
        this.apiKeyRepository = apiKeyRepository;
        this.merchantProfileRepository = merchantProfileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public ApiKeyResponse generateApiKeys(String userEmail, String environment) {
        MerchantProfile profile = merchantProfileRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new CustomException("Merchant profile not found. Create a profile first."));

        // Generate cryptographically secure random bytes
        byte[] pubBytes = new byte[24];
        byte[] secBytes = new byte[32];
        secureRandom.nextBytes(pubBytes);
        secureRandom.nextBytes(secBytes);

        // Convert to Base64 without padding and replace chars for URL safety
        String rawPublishable = Base64.getUrlEncoder().withoutPadding().encodeToString(pubBytes);
        String rawSecret = Base64.getUrlEncoder().withoutPadding().encodeToString(secBytes);

        // Add standard prefixes
        String envPrefix = environment.equalsIgnoreCase("LIVE") ? "live_" : "test_";
        String publishableKey = "pk_" + envPrefix + rawPublishable;
        String secretKey = "sk_" + envPrefix + rawSecret;

        // Create Entity and hash the secret key
        ApiKey apiKey = new ApiKey();
        apiKey.setPublishableKey(publishableKey);
        apiKey.setSecretKeyHash(passwordEncoder.encode(secretKey)); // Never store plain text
        apiKey.setEnvironment(environment.toUpperCase());
        apiKey.setMerchantProfile(profile);

        apiKeyRepository.save(apiKey);

        return new ApiKeyResponse(
                publishableKey,
                secretKey,
                apiKey.getEnvironment(),
                "WARNING: Please save this secret key. It will not be shown again!"
        );
    }
}