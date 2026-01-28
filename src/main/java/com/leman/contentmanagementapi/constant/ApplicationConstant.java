package com.leman.contentmanagementapi.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ApplicationConstant {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class HttpHeader {
        public static final String AUTHORIZATION = "Authorization";
        public static final String REFRESH_TOKEN = "X-Refresh-Token";
        public static final String CONTENT_TYPE = "Content-Type";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class HttpAttribute {
        public static final String AUTHORIZATION_PREFIX = "Bearer ";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class TokenType {
        public static final String ACCESS = "access";
        public static final String REFRESH = "refresh";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Security {
        public static final String[] PUBLIC_ENDPOINTS = {
                "/api/v1/auth/**",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/error"
        };
    }

}
