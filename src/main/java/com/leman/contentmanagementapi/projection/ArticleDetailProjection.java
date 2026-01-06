package com.leman.contentmanagementapi.projection;

import java.time.LocalDateTime;

public interface ArticleDetailProjection {

    Long getId();

    Long getCategoryId();

    String getCategoryName();

    String getTitle();

    String getContent();

    Boolean getActive();

    Boolean getPublished();

    LocalDateTime getCreatedAt();

    LocalDateTime getUpdatedAt();

}
