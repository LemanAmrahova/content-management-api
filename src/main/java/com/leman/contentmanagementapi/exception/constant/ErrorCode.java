package com.leman.contentmanagementapi.exception.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorCode {

    public static final String RESOURCE_NOT_FOUND = "{0}_NOT_FOUND";
    public static final String RESOURCE_ALREADY_EXISTS = "{0}_ALREADY_EXISTS";
    public static final String VALIDATION_ERROR = "VALIDATION_ERROR";

}
