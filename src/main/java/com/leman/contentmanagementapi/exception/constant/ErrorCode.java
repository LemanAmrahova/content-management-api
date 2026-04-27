package com.leman.contentmanagementapi.exception.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorCode {

    public static final String UNAUTHORIZED = "unauthorized";
    public static final String FORBIDDEN = "forbidden";
    public static final String BAD_REQUEST = "bad_request";
    public static final String VALIDATION_ERROR = "validation_error";
    public static final String INVALID_JSON = "invalid_json";
    public static final String RESOURCE_NOT_FOUND = "{0}_not_found";
    public static final String RESOURCE_ALREADY_EXISTS = "{0}_already_exists";

}
