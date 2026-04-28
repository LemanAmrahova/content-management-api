package com.leman.contentmanagementapi.controller;

import static com.leman.contentmanagementapi.constant.ApplicationConstant.HttpAttribute.AUTHORIZATION_PREFIX;
import static com.leman.contentmanagementapi.constant.ApplicationConstant.HttpHeader;
import static org.springframework.http.HttpStatus.CREATED;

import com.leman.contentmanagementapi.dto.request.LoginRequest;
import com.leman.contentmanagementapi.dto.request.RegisterRequest;
import com.leman.contentmanagementapi.dto.response.LoginResponse;
import com.leman.contentmanagementapi.dto.response.UserResponse;
import com.leman.contentmanagementapi.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody @Valid RegisterRequest request) {
        return ResponseEntity.status(CREATED).body(authService.registerUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@RequestHeader(HttpHeader.REFRESH_TOKEN) String refreshToken) {
        return ResponseEntity.ok(authService.refreshToken(refreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader(HttpHeader.AUTHORIZATION) String authHeader) {
        String token = authHeader.substring(AUTHORIZATION_PREFIX.length());
        authService.logout(token);
        return ResponseEntity.noContent().build();
    }

}
