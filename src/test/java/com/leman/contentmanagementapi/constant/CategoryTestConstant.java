package com.leman.contentmanagementapi.constant;

import static com.leman.contentmanagementapi.constant.TestConstant.ID;

import com.leman.contentmanagementapi.dto.request.CategoryCreateRequest;
import com.leman.contentmanagementapi.dto.request.CategoryStatusChangeRequest;
import com.leman.contentmanagementapi.dto.request.CategoryUpdateRequest;
import com.leman.contentmanagementapi.dto.response.CategoryResponse;
import com.leman.contentmanagementapi.entity.Category;

public final class CategoryTestConstant {

    private CategoryTestConstant() {
    }

    public static final String NAME = "NAME";

    public static final CategoryCreateRequest CATEGORY_CREATE_REQUEST = CategoryCreateRequest.builder()
            .name(NAME)
            .build();

    public static final CategoryUpdateRequest CATEGORY_UPDATE_REQUEST = CategoryUpdateRequest.builder()
            .name(NAME)
            .build();

    public static final CategoryStatusChangeRequest CATEGORY_STATUS_CHANGE_REQUEST =
            CategoryStatusChangeRequest.builder()
                    .active(true)
                    .build();

    public static Category categoryEntity() {
        return Category.builder()
                .id(ID)
                .name(NAME)
                .active(true)
                .build();
    }

    public static final CategoryResponse CATEGORY_RESPONSE = CategoryResponse.builder()
            .id(ID)
            .name(NAME)
            .active(true)
            .build();

}
