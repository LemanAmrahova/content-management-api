package com.leman.contentmanagementapi.exception;

import com.leman.contentmanagementapi.exception.constant.ErrorMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.text.MessageFormat;

@Data
@EqualsAndHashCode(callSuper = true)
public class DuplicateResourceException extends RuntimeException {

    private final String entity;
    private final String field;
    private final Object value;

    public DuplicateResourceException(String entity, String field, Object value) {
        super(MessageFormat.format(ErrorMessage.RESOURCE_ALREADY_EXISTS_ERROR_MESSAGE, entity, field, value));
        this.entity = entity;
        this.field = field;
        this.value = value;
    }

}
