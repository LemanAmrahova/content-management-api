package com.leman.contentmanagementapi.exception.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorMessage {

    public static final String RESOURCE_NOT_FOUND_ERROR_MESSAGE = "{0} not found with {1} - {2}";
    public static final String RESOURCE_ALREADY_EXISTS_ERROR_MESSAGE = "{0} already exists with {1} - {2}";

}
