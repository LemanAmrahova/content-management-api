package com.leman.contentmanagementapi.constant;

import com.leman.contentmanagementapi.dto.request.LoginRequest;
import com.leman.contentmanagementapi.dto.request.RegisterRequest;
import com.leman.contentmanagementapi.dto.response.LoginResponse;
import com.leman.contentmanagementapi.dto.response.UserResponse;
import com.leman.contentmanagementapi.entity.User;
import com.leman.contentmanagementapi.enums.Role;
import java.time.LocalDateTime;

import static com.leman.contentmanagementapi.constant.TestConstant.ID;

public final class AuthTestConstant {

    private AuthTestConstant() {
    }

    public static final String USERNAME = "testuser";
    public static final String EMAIL = "test@example.com";
    public static final Role ROLE = Role.ADMIN;
    public static final String PASSWORD = "Test123@";
    public static final String ENCODED_PASSWORD = "encoded-password";
    public static final Boolean ENABLED = true;
    public static final LocalDateTime CREATED_AT = LocalDateTime.of(2024, 1, 1, 10, 0);
    public static final LocalDateTime UPDATED_AT = LocalDateTime.of(2024, 1, 1, 10, 0);

    public static final String ACCESS_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsInR5cGUiOiJhY2Nlc3MifQ";
    public static final String REFRESH_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsInR5cGUiOiJyZWZyZXNoIn0";
    public static final String TOKEN_TYPE = "Bearer";

    public static final RegisterRequest REGISTER_REQUEST = RegisterRequest.builder()
            .username(USERNAME)
            .email(EMAIL)
            .password(PASSWORD)
            .build();

    public static final LoginRequest LOGIN_REQUEST = LoginRequest.builder()
            .username(USERNAME)
            .password(PASSWORD)
            .build();

    public static final User USER_ENTITY = User.builder()
            .id(ID)
            .username(USERNAME)
            .role(ROLE)
            .password(ENCODED_PASSWORD)
            .enabled(ENABLED)
            .build();

    public static final UserResponse USER_RESPONSE = UserResponse.builder()
            .id(ID)
            .username(USERNAME)
            .email(EMAIL)
            .role(ROLE)
            .createdAt(CREATED_AT)
            .updatedAt(UPDATED_AT)
            .build();

    public static final LoginResponse LOGIN_RESPONSE = LoginResponse.builder()
            .accessToken(ACCESS_TOKEN)
            .refreshToken(REFRESH_TOKEN)
            .tokenType(TOKEN_TYPE)
            .build();

}
