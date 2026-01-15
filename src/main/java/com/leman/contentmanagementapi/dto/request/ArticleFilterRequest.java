package com.leman.contentmanagementapi.dto.request;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleFilterRequest {

    @Size(min = 2, max = 100)
    private String title;

    @Size(min = 2, max = 100)
    private String content;

    @Positive
    private Long categoryId;

    private Boolean published;
    private Boolean active;

}
