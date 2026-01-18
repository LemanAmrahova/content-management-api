package com.leman.contentmanagementapi.dto.request;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ArticleFilterRequest extends PageableRequest {

    @Size(min = 2, max = 100)
    private String title;

    @Size(min = 2, max = 100)
    private String content;

    @Positive
    private Long categoryId;

    private Boolean published;
    private Boolean active;

}
