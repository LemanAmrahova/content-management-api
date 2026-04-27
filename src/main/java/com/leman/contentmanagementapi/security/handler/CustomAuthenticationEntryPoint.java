package com.leman.contentmanagementapi.security.handler;

import static com.leman.contentmanagementapi.exception.constant.ErrorMessage.UNAUTHORIZED_ERROR_MESSAGE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leman.contentmanagementapi.exception.constant.ErrorCode;
import com.leman.contentmanagementapi.exception.constant.ErrorType;
import com.leman.contentmanagementapi.exception.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(objectMapper.writeValueAsString(
                ErrorResponse.builder()
                        .errorCode(ErrorCode.UNAUTHORIZED)
                        .errorType(ErrorType.UNAUTHORIZED)
                        .errorMessage(UNAUTHORIZED_ERROR_MESSAGE)
                        .path(request.getRequestURI())
                        .timestamp(LocalDateTime.now())
                        .build()
        ));
    }

}
