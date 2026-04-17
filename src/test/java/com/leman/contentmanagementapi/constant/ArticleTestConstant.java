package com.leman.contentmanagementapi.constant;

import static com.leman.contentmanagementapi.constant.TestConstant.EMPTY;
import static com.leman.contentmanagementapi.constant.TestConstant.FIRST;
import static com.leman.contentmanagementapi.constant.TestConstant.ID;
import static com.leman.contentmanagementapi.constant.TestConstant.LAST;
import static com.leman.contentmanagementapi.constant.TestConstant.PAGE;
import static com.leman.contentmanagementapi.constant.TestConstant.SIZE;
import static com.leman.contentmanagementapi.constant.TestConstant.SORT_BY;
import static com.leman.contentmanagementapi.constant.TestConstant.SORT_DIRECTION;
import static com.leman.contentmanagementapi.constant.TestConstant.TOTAL_ELEMENTS;
import static com.leman.contentmanagementapi.constant.TestConstant.TOTAL_PAGES;

import com.leman.contentmanagementapi.dto.request.ArticleCreateRequest;
import com.leman.contentmanagementapi.dto.request.ArticleFilterRequest;
import com.leman.contentmanagementapi.dto.request.ArticleUpdateRequest;
import com.leman.contentmanagementapi.dto.response.ArticleResponse;
import com.leman.contentmanagementapi.dto.response.PageableResponse;
import com.leman.contentmanagementapi.entity.Article;
import com.leman.contentmanagementapi.entity.Category;
import java.util.List;

public final class ArticleTestConstant {

    private ArticleTestConstant() {
    }

    public static final String TITLE = "TITLE";
    public static final String CONTENT = "CONTENT";
    public static final Category CATEGORY = CategoryTestConstant.categoryEntity();
    public static final Long CATEGORY_ID = 1L;

    public static final ArticleCreateRequest ARTICLE_CREATE_REQUEST = ArticleCreateRequest.builder()
            .title(TITLE)
            .content(CONTENT)
            .categoryId(CATEGORY_ID)
            .build();

    public static final ArticleFilterRequest ARTICLE_FILTER_REQUEST = ArticleFilterRequest.builder()
            .title(TITLE)
            .content(CONTENT)
            .categoryId(CATEGORY_ID)
            .published(false)
            .active(true)
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

    public static Article articleEntity() {
        return Article.builder()
                .id(ID)
                .title(TITLE)
                .content(CONTENT)
                .category(CATEGORY)
                .active(true)
                .published(false)
                .build();
    }

    public static final ArticleResponse ARTICLE_RESPONSE = ArticleResponse.builder()
            .id(ID)
            .title(TITLE)
            .content(CONTENT)
            .categoryId(CATEGORY_ID)
            .active(true)
            .published(false)
            .build();

    public static final ArticleResponse ARTICLE_DETAIL_RESPONSE = ArticleResponse.builder()
            .id(ID)
            .title(TITLE)
            .content(CONTENT)
            .categoryId(CATEGORY_ID)
            .categoryName(CATEGORY.getName())
            .active(true)
            .published(false)
            .build();

    public static final PageableResponse<ArticleResponse> PAGEABLE_ARTICLE_RESPONSE =
            PageableResponse.<ArticleResponse>builder()
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
