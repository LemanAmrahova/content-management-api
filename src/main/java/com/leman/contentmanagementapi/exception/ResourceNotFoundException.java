package com.leman.contentmanagementapi.exception;

import com.leman.contentmanagementapi.exception.constant.ErrorMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.text.MessageFormat;

@Data
@EqualsAndHashCode(callSuper = true)
public class ResourceNotFoundException extends RuntimeException {

    private final String entity;
    private final String field;
    private final Object value;

    public ResourceNotFoundException(String entity, String field, Object value) {
        super(MessageFormat.format(ErrorMessage.RESOURCE_NOT_FOUND_ERROR_MESSAGE, entity, field, value));
        this.entity = entity;
        this.field = field;
        this.value = value;
    }

}
