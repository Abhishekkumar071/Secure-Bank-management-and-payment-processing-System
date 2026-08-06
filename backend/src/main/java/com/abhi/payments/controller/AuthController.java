package com.abhi.payments.controller;

import com.abhi.payments.dto.ApiResponse;
import com.abhi.payments.dto.RegisterRequest;
import com.abhi.payments.entity.User;
import com.abhi.payments.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Object>> register(@Valid @RequestBody RegisterRequest request) {
        User savedUser = userService.registerUser(request);

        // We shouldn't send the hashed password back in response
        savedUser.setPassword(null);

        ApiResponse<Object> response = new ApiResponse<>(true, "User registered successfully", savedUser);
        return ResponseEntity.ok(response);
    }
}