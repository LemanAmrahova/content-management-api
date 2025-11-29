package com.leman.contentmanagementapi.mapper;

import com.leman.contentmanagementapi.dto.request.CategoryCreateRequest;
import com.leman.contentmanagementapi.dto.response.CategoryResponse;
import com.leman.contentmanagementapi.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(
        componentModel = SPRING,
        unmappedSourcePolicy = IGNORE,
        unmappedTargetPolicy = IGNORE,
        injectionStrategy = CONSTRUCTOR
)
public interface CategoryMapper {

    @Mapping(target = "isActive", constant = "true")
    Category toEntity(CategoryCreateRequest request);

    CategoryResponse toResponse(Category entity);

    List<CategoryResponse> toResponse(List<Category> entities);

}
