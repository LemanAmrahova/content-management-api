package com.leman.contentmanagementapi.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private String errorCode;
    private String errorType;
    private String errorMessage;
    private String path;
    private LocalDateTime timestamp;

}
