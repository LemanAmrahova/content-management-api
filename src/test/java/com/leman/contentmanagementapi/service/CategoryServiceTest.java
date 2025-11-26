package com.leman.contentmanagementapi.service;

import com.leman.contentmanagementapi.dto.response.CategoryResponse;
import com.leman.contentmanagementapi.mapper.CategoryMapper;
import com.leman.contentmanagementapi.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.leman.contentmanagementapi.constant.CategoryTestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void createCategory_ShouldReturn_Success() {
        given(categoryMapper.toEntity(CREATE_CATEGORY_REQUEST)).willReturn(CATEGORY_ENTITY);
        given(categoryRepository.save(CATEGORY_ENTITY)).willReturn(CATEGORY_ENTITY);
        given(categoryMapper.toResponse(CATEGORY_ENTITY)).willReturn(CATEGORY_RESPONSE);

        CategoryResponse result = categoryService.createCategory(CREATE_CATEGORY_REQUEST);
        assertThat(result).isEqualTo(CATEGORY_RESPONSE);

        then(categoryMapper).should().toEntity(CREATE_CATEGORY_REQUEST);
        then(categoryRepository).should().save(CATEGORY_ENTITY);
        then(categoryMapper).should().toResponse(CATEGORY_ENTITY);
    }

}
