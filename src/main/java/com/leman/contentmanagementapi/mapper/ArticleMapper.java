package com.leman.contentmanagementapi.mapper;

import com.leman.contentmanagementapi.dto.request.ArticleCreateRequest;
import com.leman.contentmanagementapi.dto.response.ArticleDetailResponse;
import com.leman.contentmanagementapi.dto.response.ArticleResponse;
import com.leman.contentmanagementapi.dto.response.PageableResponse;
import com.leman.contentmanagementapi.entity.Article;
import com.leman.contentmanagementapi.projection.ArticleDetailProjection;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(
        componentModel = SPRING,
        unmappedSourcePolicy = IGNORE,
        unmappedTargetPolicy = IGNORE,
        injectionStrategy = CONSTRUCTOR
)
public interface ArticleMapper {

    @Mapping(target = "category", ignore = true)
    Article toEntity(ArticleCreateRequest request);

    @Mapping(target = "categoryId", source = "category.id")
    ArticleResponse toResponse(Article article);

    @Mapping(target = "page", source = "number")
    PageableResponse<ArticleDetailResponse> toResponse(Page<Article> articles);

    ArticleDetailResponse toDetailResponse(ArticleDetailProjection projection);

    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    ArticleDetailResponse toDetailResponse(Article article);

    @AfterMapping
    default void setDefaults(@MappingTarget Article article) {
        article.setActive(true);
        article.setPublished(false);
    }

}

