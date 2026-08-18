package com.abhi.payments.security;

import com.abhi.payments.entity.ApiKey;
import com.abhi.payments.entity.User;
import com.abhi.payments.repository.ApiKeyRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    private final ApiKeyRepository apiKeyRepository;
    private final PasswordEncoder passwordEncoder;

    public ApiKeyFilter(ApiKeyRepository apiKeyRepository, PasswordEncoder passwordEncoder) {
        this.apiKeyRepository = apiKeyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // Sirf external APIs ke liye ye filter apply hoga
        String path = request.getRequestURI();
        if (!path.startsWith("/api/external/")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Custom headers for API Keys
        String publishableKey = request.getHeader("X-Publishable-Key");
        String secretKey = request.getHeader("X-Secret-Key");

        if (publishableKey == null || secretKey == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Missing API Keys in headers");
            return;
        }

        // O(log N) Database Lookup using Indexed Publishable Key
        Optional<ApiKey> apiKeyOpt = apiKeyRepository.findByPublishableKey(publishableKey);

        if (apiKeyOpt.isPresent()) {
            ApiKey apiKeyRecord = apiKeyOpt.get();

            // O(1) BCrypt Match
            if (apiKeyRecord.isActive() && passwordEncoder.matches(secretKey, apiKeyRecord.getSecretKeyHash())) {

                User merchantUser = apiKeyRecord.getMerchantProfile().getUser();

                // Authentication successful, set Spring Security Context
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        merchantUser.getEmail(), // Treating email as principal for consistency
                        null,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_MERCHANT"))
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
                filterChain.doFilter(request, response);
                return;
            }
        }

        // If keys don't match or not found
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Invalid API Keys");
    }
}