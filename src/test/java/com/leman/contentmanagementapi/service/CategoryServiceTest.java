package com.leman.contentmanagementapi.service;

import com.leman.contentmanagementapi.dto.request.CategoryStatusChangeRequest;
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

import static com.leman.contentmanagementapi.constant.CategoryTestConstant.CATEGORY_CREATE_REQUEST;
import static com.leman.contentmanagementapi.constant.CategoryTestConstant.CATEGORY_ENTITY;
import static com.leman.contentmanagementapi.constant.CategoryTestConstant.CATEGORY_RESPONSE;
import static com.leman.contentmanagementapi.constant.CategoryTestConstant.CATEGORY_UPDATE_REQUEST;
import static com.leman.contentmanagementapi.constant.TestConstant.ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
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
        given(categoryRepository.existsByName(CATEGORY_CREATE_REQUEST.getName())).willReturn(false);
        given(categoryMapper.toEntity(CATEGORY_CREATE_REQUEST)).willReturn(CATEGORY_ENTITY);
        given(categoryRepository.save(CATEGORY_ENTITY)).willReturn(CATEGORY_ENTITY);
        given(categoryMapper.toResponse(CATEGORY_ENTITY)).willReturn(CATEGORY_RESPONSE);

        CategoryResponse result = categoryService.createCategory(CATEGORY_CREATE_REQUEST);
        assertThat(result).isEqualTo(CATEGORY_RESPONSE);

        then(categoryMapper).should().toEntity(CATEGORY_CREATE_REQUEST);
        then(categoryRepository).should().save(CATEGORY_ENTITY);
        then(categoryMapper).should().toResponse(CATEGORY_ENTITY);
    }

    @Test
    void createCategory_ShouldThrow_DuplicateResourceException() {
        given(categoryRepository.existsByName(CATEGORY_CREATE_REQUEST.getName())).willReturn(true);

        assertThatThrownBy(() -> categoryService.createCategory(CATEGORY_CREATE_REQUEST))
                .isInstanceOf(DuplicateResourceException.class);

        then(categoryRepository).should().existsByName(CATEGORY_CREATE_REQUEST.getName());
        then(categoryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void findAllCategories_ShouldReturn_Success() {
        given(categoryRepository.findAllByActiveTrue()).willReturn(List.of(CATEGORY_ENTITY));
        given(categoryMapper.toResponse(List.of(CATEGORY_ENTITY))).willReturn(List.of(CATEGORY_RESPONSE));

        assertThat(categoryService.findAllCategories()).isEqualTo(List.of(CATEGORY_RESPONSE));

        then(categoryRepository).should().findAllByActiveTrue();
    }

    @Test
    void findCategoryById_ShouldReturn_Success() {
        given(categoryRepository.findByIdAndActiveTrue(ID)).willReturn(Optional.of(CATEGORY_ENTITY));
        given(categoryMapper.toResponse(CATEGORY_ENTITY)).willReturn(CATEGORY_RESPONSE);

        assertThat(categoryService.findCategoryById(ID)).isEqualTo(CATEGORY_RESPONSE);

        then(categoryRepository).should().findByIdAndActiveTrue(ID);
        then(categoryMapper).should().toResponse(CATEGORY_ENTITY);
    }

    @Test
    void updateCategory_ShouldReturnResponse() {
        given(categoryRepository.findById(ID)).willReturn(Optional.of(CATEGORY_ENTITY));
        given(categoryRepository.existsByNameAndIdNot(CATEGORY_UPDATE_REQUEST.getName(), ID)).willReturn(false);
        given(categoryMapper.toResponse(CATEGORY_ENTITY)).willReturn(CATEGORY_RESPONSE);

        assertThat(categoryService.updateCategory(ID, CATEGORY_UPDATE_REQUEST)).isEqualTo(CATEGORY_RESPONSE);

        then(categoryRepository).should().findById(ID);
        then(categoryRepository).should().existsByNameAndIdNot(CATEGORY_UPDATE_REQUEST.getName(), ID);
        then(categoryMapper).should().toResponse(CATEGORY_ENTITY);
    }

    @Test
    void updateCategory_ShouldThrow_DuplicateResourceException() {
        given(categoryRepository.findById(ID)).willReturn(Optional.of(CATEGORY_ENTITY));
        given(categoryRepository.existsByNameAndIdNot(CATEGORY_UPDATE_REQUEST.getName(), ID)).willReturn(true);

        assertThatThrownBy(() -> categoryService.updateCategory(ID, CATEGORY_UPDATE_REQUEST))
                .isInstanceOf(DuplicateResourceException.class);

        then(categoryRepository).should().findById(ID);
        then(categoryRepository).should().existsByNameAndIdNot(CATEGORY_UPDATE_REQUEST.getName(), ID);
        then(categoryMapper).shouldHaveNoInteractions();
    }

    @Test
    void changeCategoryStatus_ShouldReturn_Success() {
        given(categoryRepository.findById(ID)).willReturn(Optional.of(CATEGORY_ENTITY));

        categoryService.changeCategoryStatus(ID, CategoryStatusChangeRequest.builder().active(false).build());

        then(categoryRepository).should().findById(ID);
        then(categoryRepository).shouldHaveNoMoreInteractions();
    }

}
