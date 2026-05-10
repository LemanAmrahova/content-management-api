package com.leman.contentmanagementapi.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestConstant {

    public static final Long ID = 1L;
    public static final Integer PAGE = 0;
    public static final Integer SIZE = 10;
    public static final String SORT_BY = "id";
    public static final Sort.Direction SORT_DIRECTION = Sort.Direction.ASC;
    public static final long TOTAL_ELEMENTS = 1L;
    public static final int TOTAL_PAGES = 1;
    public static final boolean FIRST = true;
    public static final boolean LAST = true;
    public static final boolean EMPTY = false;

}
