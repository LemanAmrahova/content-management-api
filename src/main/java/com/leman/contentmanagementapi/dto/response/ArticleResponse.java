package com.leman.contentmanagementapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleResponse {

    private Long id;
    private Long categoryId;
    private String title;
    private String content;
    private Boolean active;
    private Boolean published;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
