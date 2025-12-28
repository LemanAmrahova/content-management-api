package com.leman.contentmanagementapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleCreateRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotNull
    @Positive
    private Long categoryId;

}
