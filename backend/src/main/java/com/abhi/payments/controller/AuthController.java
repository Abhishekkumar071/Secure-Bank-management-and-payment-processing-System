package com.abhi.payments.controller;

import com.abhi.payments.dto.ApiResponse;
import com.abhi.payments.dto.LoginRequest;
import com.abhi.payments.dto.LoginResponse;
import com.abhi.payments.dto.RegisterRequest;
import com.abhi.payments.entity.User;
import com.abhi.payments.exception.CustomException;
import com.abhi.payments.repository.UserRepository;
import com.abhi.payments.security.CustomUserDetailsService;
import com.abhi.payments.security.JwtUtil;
import com.abhi.payments.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public AuthController(UserService userService, AuthenticationManager authenticationManager,
                          CustomUserDetailsService userDetailsService, JwtUtil jwtUtil, UserRepository userRepository) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Object>> register(@Valid @RequestBody RegisterRequest request) {
        User savedUser = userService.registerUser(request);
        savedUser.setPassword(null);
        return ResponseEntity.ok(new ApiResponse<>(true, "User registered successfully", savedUser));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        try {
            // This will internally verify password using our CustomUserDetailsService
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );
        } catch (Exception e) {
            throw new CustomException("Invalid email or password");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.email());
        User user = userRepository.findByEmail(request.email()).orElseThrow();

        String jwtToken = jwtUtil.generateToken(userDetails, user.getRole().name());

        LoginResponse loginResponse = new LoginResponse(jwtToken, user.getEmail(), user.getRole().name());

        return ResponseEntity.ok(new ApiResponse<>(true, "Login successful", loginResponse));
    }
}