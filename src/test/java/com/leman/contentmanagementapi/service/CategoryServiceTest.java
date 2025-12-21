package com.leman.contentmanagementapi.service;

import com.leman.contentmanagementapi.dto.request.CategoryUpdateRequest;
import com.leman.contentmanagementapi.dto.response.CategoryResponse;
import com.leman.contentmanagementapi.exception.DuplicateResourceException;
import com.leman.contentmanagementapi.mapper.CategoryMapper;
import com.leman.contentmanagementapi.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;

import static com.leman.contentmanagementapi.constant.CategoryTestConstant.*;
import static com.leman.contentmanagementapi.constant.TestConstant.ID;
import static com.leman.contentmanagementapi.constant.TestConstant.NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

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
        given(categoryRepository.existsByName(NAME)).willReturn(false);
        given(categoryMapper.toEntity(CREATE_CATEGORY_REQUEST)).willReturn(CATEGORY_ENTITY);
        given(categoryRepository.save(CATEGORY_ENTITY)).willReturn(CATEGORY_ENTITY);
        given(categoryMapper.toResponse(CATEGORY_ENTITY)).willReturn(CATEGORY_RESPONSE);

        CategoryResponse result = categoryService.createCategory(CREATE_CATEGORY_REQUEST);
        assertThat(result).isEqualTo(CATEGORY_RESPONSE);

        then(categoryMapper).should().toEntity(CREATE_CATEGORY_REQUEST);
        then(categoryRepository).should().save(CATEGORY_ENTITY);
        then(categoryMapper).should().toResponse(CATEGORY_ENTITY);
    }

    @Test
    void createCategory_ShouldThrow_DuplicateResourceException() {
        given(categoryRepository.existsByName(NAME)).willReturn(true);

        assertThatThrownBy(() -> categoryService.createCategory(CREATE_CATEGORY_REQUEST))
                .isInstanceOf(DuplicateResourceException.class);

        then(categoryRepository).should().existsByName(NAME);
        then(categoryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void findAllCategories_ShouldReturn_Success() {
        given(categoryRepository.findAll()).willReturn(List.of(CATEGORY_ENTITY));
        given(categoryMapper.toResponse(List.of(CATEGORY_ENTITY))).willReturn(List.of(CATEGORY_RESPONSE));

        assertThat(categoryService.findAllCategories()).isEqualTo(List.of(CATEGORY_RESPONSE));

        then(categoryRepository).should().findAll();
    }

    @Test
    void findCategoryById_ShouldReturn_Success() {
        given(categoryRepository.findById(ID)).willReturn(Optional.of(CATEGORY_ENTITY));
        given(categoryMapper.toResponse(CATEGORY_ENTITY)).willReturn(CATEGORY_RESPONSE);

        assertThat(categoryService.findCategoryById(ID)).isEqualTo(CATEGORY_RESPONSE);

        then(categoryRepository).should().findById(ID);
        then(categoryMapper).should().toResponse(CATEGORY_ENTITY);
    }

    @Test
    void updateCategory_ShouldUpdateAndReturnResponse() {
        CategoryUpdateRequest request = CategoryUpdateRequest.builder().name(NAME).build();

        given(categoryRepository.findById(ID)).willReturn(Optional.of(CATEGORY_ENTITY));
        given(categoryRepository.save(CATEGORY_ENTITY)).willReturn(CATEGORY_ENTITY);
        given(categoryMapper.toResponse(CATEGORY_ENTITY)).willReturn(CATEGORY_RESPONSE);

        CategoryResponse result = categoryService.updateCategory(ID, request);
        assertThat(result).isEqualTo(CATEGORY_RESPONSE);
        assertThat(CATEGORY_ENTITY.getName()).isEqualTo(NAME);

        then(categoryRepository).should().findById(ID);
        then(categoryRepository).should().save(CATEGORY_ENTITY);
        then(categoryMapper).should().toResponse(CATEGORY_ENTITY);
    }

    @Test
    void delete_ShouldReturn_Success() {
        given(categoryRepository.findById(ID)).willReturn(Optional.of(CATEGORY_ENTITY));
        willDoNothing().given(categoryRepository).deactivateById(ID);

        categoryService.deleteCategory(ID);

        then(categoryRepository).should().findById(ID);
        then(categoryRepository).should().deactivateById(ID);
        then(categoryRepository).shouldHaveNoMoreInteractions();
    }

}
