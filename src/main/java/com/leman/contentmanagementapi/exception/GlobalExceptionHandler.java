package com.leman.contentmanagementapi.exception;

import com.leman.contentmanagementapi.exception.constant.ErrorCode;
import com.leman.contentmanagementapi.exception.constant.ErrorMessage;
import com.leman.contentmanagementapi.exception.constant.ErrorType;
import com.leman.contentmanagementapi.exception.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.text.MessageFormat;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex,
                                                                HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ErrorResponse.builder()
                        .errorCode(MessageFormat.format(ErrorCode.RESOURCE_NOT_FOUND, ex.getEntity().toUpperCase()))
                        .errorType(ErrorType.NOT_FOUND)
                        .errorMessage(MessageFormat
                                .format(ErrorMessage.RESOURCE_NOT_FOUND_ERROR_MESSAGE, ex.getEntity(),
                                        ex.getField(), ex.getValue()))
                        .path(request.getRequestURI())
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

}
