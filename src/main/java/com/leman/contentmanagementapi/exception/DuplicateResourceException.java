package com.leman.contentmanagementapi.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DuplicateResourceException extends RuntimeException {

    private final String entity;
    private final String field;
    private final Object value;

    public DuplicateResourceException(String entity, String field, Object value) {
        super();
        this.entity = entity;
        this.field = field;
        this.value = value;
    }

}
