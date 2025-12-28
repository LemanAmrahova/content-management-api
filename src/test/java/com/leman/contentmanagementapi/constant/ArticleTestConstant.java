package com.leman.contentmanagementapi.constant;

import com.leman.contentmanagementapi.dto.request.ArticleCreateRequest;
import com.leman.contentmanagementapi.dto.response.ArticleResponse;
import com.leman.contentmanagementapi.entity.Article;
import com.leman.contentmanagementapi.entity.Category;

import static com.leman.contentmanagementapi.constant.TestConstant.ACTIVE;
import static com.leman.contentmanagementapi.constant.TestConstant.ID;

public final class ArticleTestConstant {

    private ArticleTestConstant() {
    }

    private static final String TITLE = "TITLE";
    private static final String CONTENT = "CONTENT";
    private static final Category CATEGORY = CategoryTestConstant.CATEGORY_ENTITY;
    private static final Long CATEGORY_ID = 1L;
    private static final Boolean PUBLISHED = false;

    public static final ArticleCreateRequest ARTICLE_CREATE_REQUEST = ArticleCreateRequest.builder()
            .title(TITLE)
            .content(CONTENT)
            .categoryId(CATEGORY_ID)
            .build();

    public static final Article ARTICLE_ENTITY = Article.builder()
            .id(ID)
            .title(TITLE)
            .content(CONTENT)
            .category(CATEGORY)
            .active(ACTIVE)
            .published(PUBLISHED)
            .build();

    public static final ArticleResponse ARTICLE_RESPONSE = ArticleResponse.builder()
            .id(ID)
            .title(TITLE)
            .content(CONTENT)
            .categoryId(CATEGORY_ID)
            .active(ACTIVE)
            .published(PUBLISHED)
            .build();

}
