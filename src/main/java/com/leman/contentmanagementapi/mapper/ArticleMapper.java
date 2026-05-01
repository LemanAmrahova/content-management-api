package com.leman.contentmanagementapi.mapper;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.ReportingPolicy.IGNORE;

import com.leman.contentmanagementapi.dto.request.ArticleCreateRequest;
import com.leman.contentmanagementapi.dto.response.ArticleResponse;
import com.leman.contentmanagementapi.dto.response.PageableResponse;
import com.leman.contentmanagementapi.entity.Article;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(
        componentModel = SPRING,
        unmappedSourcePolicy = IGNORE,
        unmappedTargetPolicy = IGNORE,
        injectionStrategy = CONSTRUCTOR
)
public interface ArticleMapper {

    @Mapping(target = "active", constant = "true")
    @Mapping(target = "published", constant = "false")
    @Mapping(target = "category.id", source = "categoryId")
    Article toEntity(ArticleCreateRequest request);

    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "authorId", source = "author.id")
    ArticleResponse toResponse(Article article);

    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "authorUsername", source = "author.username")
    ArticleResponse toDetailResponse(Article article);

    default PageableResponse<ArticleResponse> toPageableResponse(Page<Article> articles) {
        return PageableResponse.<ArticleResponse>builder()
                .content(articles.getContent().stream()
                        .map(this::toDetailResponse)
                        .toList())
                .page(articles.getNumber())
                .size(articles.getSize())
                .last(articles.isLast())
                .totalPages(articles.getTotalPages())
                .totalElements(articles.getTotalElements())
                .build();
    }

}

