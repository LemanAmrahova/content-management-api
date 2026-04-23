package com.leman.contentmanagementapi.constant;

import com.leman.contentmanagementapi.dto.request.UserUpdateRequest;
import com.leman.contentmanagementapi.dto.response.UserResponse;
import com.leman.contentmanagementapi.entity.User;
import com.leman.contentmanagementapi.enums.Role;
import com.leman.contentmanagementapi.security.UserPrincipal;
import java.time.LocalDateTime;

public final class UserTestConstant {

    private UserTestConstant() {
    }

    public static final Long USER_ID = 1L;
    public static final String USERNAME = "testuser";
    public static final String EMAIL = "test@example.com";
    public static final String ENCODED_PASSWORD = "$2a$10$encodedPassword";
    public static final LocalDateTime CREATED_AT = LocalDateTime.of(2024, 1, 1, 10, 0);
    public static final LocalDateTime UPDATED_AT = LocalDateTime.of(2024, 1, 1, 10, 0);

    public static final UserUpdateRequest USER_UPDATE_REQUEST = UserUpdateRequest.builder()
            .username(USERNAME)
            .email(EMAIL)
            .build();

    public static final User USER_ENTITY = User.builder()
            .id(USER_ID)
            .username(USERNAME)
            .email(EMAIL)
            .password(ENCODED_PASSWORD)
            .role(Role.USER)
            .enabled(true)
            .createdAt(CREATED_AT)
            .updatedAt(UPDATED_AT)
            .build();

    public static final UserResponse USER_RESPONSE = UserResponse.builder()
            .id(USER_ID)
            .username(USERNAME)
            .email(EMAIL)
            .role(Role.USER)
            .enabled(true)
            .createdAt(CREATED_AT)
            .updatedAt(UPDATED_AT)
            .build();

    public static final UserPrincipal USER_PRINCIPAL = new UserPrincipal(USER_ENTITY);

}
