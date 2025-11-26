package com.leman.contentmanagementapi.constant;

import com.leman.contentmanagementapi.dto.request.CategoryCreateRequest;
import com.leman.contentmanagementapi.dto.response.CategoryResponse;
import com.leman.contentmanagementapi.entity.Category;

import static com.leman.contentmanagementapi.constant.TestConstant.ID;
import static com.leman.contentmanagementapi.constant.TestConstant.NAME;

public final class CategoryTestConstant {

    private CategoryTestConstant() {
    }

    public static final CategoryCreateRequest CREATE_CATEGORY_REQUEST =
            CategoryCreateRequest.builder()
                    .name(NAME)
                    .build();

    public static final Category CATEGORY_ENTITY = Category.builder()
            .id(ID)
            .name(NAME)
            .isActive(true)
            .build();

    public static final CategoryResponse CATEGORY_RESPONSE = CategoryResponse.builder()
            .id(ID)
            .name(NAME)
            .build();

}
