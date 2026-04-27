package com.leman.contentmanagementapi.security.handler;

import static com.leman.contentmanagementapi.exception.constant.ErrorMessage.FORBIDDEN_ERROR_MESSAGE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leman.contentmanagementapi.exception.constant.ErrorCode;
import com.leman.contentmanagementapi.exception.constant.ErrorType;
import com.leman.contentmanagementapi.exception.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write(objectMapper.writeValueAsString(
                ErrorResponse.builder()
                        .errorCode(ErrorCode.FORBIDDEN)
                        .errorType(ErrorType.FORBIDDEN)
                        .errorMessage(FORBIDDEN_ERROR_MESSAGE)
                        .path(request.getRequestURI())
                        .timestamp(LocalDateTime.now())
                        .build()
        ));
    }

}
