package com.leman.contentmanagementapi.mapper;

import com.leman.contentmanagementapi.dto.request.ArticleCreateRequest;
import com.leman.contentmanagementapi.dto.response.ArticleDetailResponse;
import com.leman.contentmanagementapi.dto.response.ArticleResponse;
import com.leman.contentmanagementapi.entity.Article;
import com.leman.contentmanagementapi.projection.ArticleDetailProjection;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

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

    ArticleDetailResponse toDetailResponse(ArticleDetailProjection projection);

    @AfterMapping
    default void setDefaults(@MappingTarget Article article) {
        article.setActive(true);
        article.setPublished(false);
    }

}

