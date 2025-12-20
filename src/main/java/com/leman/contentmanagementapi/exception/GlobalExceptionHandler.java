package com.leman.contentmanagementapi.exception;

import com.leman.contentmanagementapi.exception.constant.ErrorCode;
import com.leman.contentmanagementapi.exception.constant.ErrorMessage;
import com.leman.contentmanagementapi.exception.constant.ErrorType;
import com.leman.contentmanagementapi.exception.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ErrorResponse> buildErrorResponse(String code, String type, String message,
                                                             HttpServletRequest request, HttpStatus status) {
        return ResponseEntity.status(status).body(
                ErrorResponse.builder()
                        .errorCode(code)
                        .errorType(type)
                        .errorMessage(message)
                        .path(request.getRequestURI())
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex,
                                                                HttpServletRequest request) {
        String message = MessageFormat.format(ErrorMessage.RESOURCE_NOT_FOUND_ERROR_MESSAGE,
                ex.getEntity(), ex.getField(), ex.getValue());
        String code = MessageFormat.format(ErrorCode.RESOURCE_NOT_FOUND, ex.getEntity().toUpperCase());

        return buildErrorResponse(code, ErrorType.NOT_FOUND, message, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResource(DuplicateResourceException ex,
                                                                 HttpServletRequest request) {
        String message = MessageFormat.format(ErrorMessage.RESOURCE_ALREADY_EXISTS_ERROR_MESSAGE,
                ex.getEntity(), ex.getField(), ex.getValue());
        String code = MessageFormat.format(ErrorCode.RESOURCE_ALREADY_EXISTS, ex.getEntity().toUpperCase());

        return buildErrorResponse(code, ErrorType.CONFLICT, message, request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex,
                                                                    HttpServletRequest request) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + " " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return buildErrorResponse(ErrorCode.VALIDATION_ERROR, ErrorType.BAD_REQUEST,
                message, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex,
                                                                            HttpServletRequest request) {
        String message = ex.getConstraintViolations()
                .stream()
                .map(v -> {
                    String field = v.getPropertyPath().toString();
                    field = field.contains(".") ? field.substring(field.lastIndexOf('.') + 1) : field;
                    return field + " " + v.getMessage();
                })
                .collect(Collectors.joining("; "));

        return buildErrorResponse(ErrorCode.VALIDATION_ERROR, ErrorType.BAD_REQUEST,
                message, request, HttpStatus.BAD_REQUEST);
    }

}
