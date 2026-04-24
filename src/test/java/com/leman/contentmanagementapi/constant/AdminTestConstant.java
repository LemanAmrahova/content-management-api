package com.leman.contentmanagementapi.constant;

import static com.leman.contentmanagementapi.constant.TestConstant.EMPTY;
import static com.leman.contentmanagementapi.constant.TestConstant.FIRST;
import static com.leman.contentmanagementapi.constant.TestConstant.LAST;
import static com.leman.contentmanagementapi.constant.TestConstant.PAGE;
import static com.leman.contentmanagementapi.constant.TestConstant.SIZE;
import static com.leman.contentmanagementapi.constant.TestConstant.SORT_BY;
import static com.leman.contentmanagementapi.constant.TestConstant.SORT_DIRECTION;
import static com.leman.contentmanagementapi.constant.TestConstant.TOTAL_ELEMENTS;
import static com.leman.contentmanagementapi.constant.TestConstant.TOTAL_PAGES;
import static com.leman.contentmanagementapi.constant.UserTestConstant.USER_RESPONSE;

import com.leman.contentmanagementapi.dto.request.UserFilterRequest;
import com.leman.contentmanagementapi.dto.response.PageableResponse;
import com.leman.contentmanagementapi.dto.response.UserResponse;
import java.util.List;

public final class AdminTestConstant {

    public static final UserFilterRequest USER_FILTER_REQUEST = UserFilterRequest.builder()
            .page(PAGE)
            .size(SIZE)
            .sortBy(SORT_BY)
            .sortDirection(SORT_DIRECTION)
            .build();

    public static final PageableResponse<UserResponse> PAGEABLE_USER_RESPONSE = PageableResponse.<UserResponse>builder()
            .content(List.of(USER_RESPONSE))
            .page(PAGE)
            .size(SIZE)
            .totalElements(TOTAL_ELEMENTS)
            .totalPages(TOTAL_PAGES)
            .first(FIRST)
            .last(LAST)
            .empty(EMPTY)
            .build();

}
