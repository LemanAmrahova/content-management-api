package com.leman.contentmanagementapi.constant;

import com.leman.contentmanagementapi.dto.request.ArticleCreateRequest;
import com.leman.contentmanagementapi.dto.request.ArticleFilterRequest;
import com.leman.contentmanagementapi.dto.request.ArticleUpdateRequest;
import com.leman.contentmanagementapi.dto.response.ArticleDetailResponse;
import com.leman.contentmanagementapi.dto.response.ArticleResponse;
import com.leman.contentmanagementapi.dto.response.PageableResponse;
import com.leman.contentmanagementapi.entity.Article;
import com.leman.contentmanagementapi.entity.Category;
import java.util.List;

import static com.leman.contentmanagementapi.constant.TestConstant.ACTIVE;
import static com.leman.contentmanagementapi.constant.TestConstant.ID;
import static com.leman.contentmanagementapi.constant.TestConstant.PAGE;
import static com.leman.contentmanagementapi.constant.TestConstant.SIZE;
import static com.leman.contentmanagementapi.constant.TestConstant.SORT_BY;
import static com.leman.contentmanagementapi.constant.TestConstant.SORT_DIRECTION;
import static com.leman.contentmanagementapi.constant.TestConstant.TOTAL_ELEMENTS;
import static com.leman.contentmanagementapi.constant.TestConstant.TOTAL_PAGES;
import static com.leman.contentmanagementapi.constant.TestConstant.FIRST;
import static com.leman.contentmanagementapi.constant.TestConstant.LAST;
import static com.leman.contentmanagementapi.constant.TestConstant.EMPTY;

public final class ArticleTestConstant {

    private ArticleTestConstant() {
    }

    public static final String TITLE = "TITLE";
    public static final String CONTENT = "CONTENT";
    public static final Category CATEGORY = CategoryTestConstant.CATEGORY_ENTITY;
    public static final Long CATEGORY_ID = 1L;
    public static final Boolean PUBLISHED = false;

    public static final ArticleCreateRequest ARTICLE_CREATE_REQUEST = ArticleCreateRequest.builder()
            .title(TITLE)
            .content(CONTENT)
            .categoryId(CATEGORY_ID)
            .build();

    public static final ArticleFilterRequest ARTICLE_FILTER_REQUEST = ArticleFilterRequest.builder()
            .title(TITLE)
            .content(CONTENT)
            .categoryId(CATEGORY_ID)
            .published(PUBLISHED)
            .active(ACTIVE)
            .page(PAGE)
            .size(SIZE)
            .sortBy(SORT_BY)
            .sortDirection(SORT_DIRECTION)
            .build();

    public static final ArticleUpdateRequest ARTICLE_UPDATE_REQUEST = ArticleUpdateRequest.builder()
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

    public static final ArticleDetailResponse ARTICLE_DETAIL_RESPONSE = ArticleDetailResponse.builder()
            .id(ID)
            .title(TITLE)
            .content(CONTENT)
            .categoryId(CATEGORY_ID)
            .categoryName(CATEGORY.getName())
            .active(ACTIVE)
            .published(PUBLISHED)
            .build();

    public static final PageableResponse<ArticleDetailResponse> PAGEABLE_ARTICLE_RESPONSE =
            PageableResponse.<ArticleDetailResponse>builder()
                    .content(List.of(ARTICLE_DETAIL_RESPONSE))
                    .page(PAGE)
                    .size(SIZE)
                    .totalElements(TOTAL_ELEMENTS)
                    .totalPages(TOTAL_PAGES)
                    .first(FIRST)
                    .last(LAST)
                    .empty(EMPTY)
                    .build();

}
