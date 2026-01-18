package com.leman.contentmanagementapi.util;

import com.leman.contentmanagementapi.dto.request.PageableRequest;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.util.Optional;

@UtilityClass
public class PaginationUtil {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 10;
    private static final String DEFAULT_SORT_BY = "id";
    private static final Sort.Direction DEFAULT_SORT_DIRECTION = Sort.Direction.ASC;

    public static Pageable createPageable(PageableRequest request) {
        if (request == null) {
            return PageRequest.of(DEFAULT_PAGE, DEFAULT_SIZE, Sort.by(DEFAULT_SORT_DIRECTION, DEFAULT_SORT_BY));
        }

        return PageRequest.of(
                Optional.ofNullable(request.getPage()).orElse(DEFAULT_PAGE),
                Optional.ofNullable(request.getSize()).orElse(DEFAULT_SIZE),
                Sort.by(
                        Optional.ofNullable(request.getSortDirection()).orElse(DEFAULT_SORT_DIRECTION),
                        Optional.ofNullable(request.getSortBy()).orElse(DEFAULT_SORT_BY)
                )
        );
    }

}
