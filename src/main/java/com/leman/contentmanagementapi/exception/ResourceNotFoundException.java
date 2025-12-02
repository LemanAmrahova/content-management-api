package com.leman.contentmanagementapi.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ResourceNotFoundException extends RuntimeException {

    private final String entity;
    private final String field;
    private final Object value;

    public ResourceNotFoundException(String entity, String field, Object value) {
        super();
        this.entity = entity;
        this.field = field;
        this.value = value;
    }

}
