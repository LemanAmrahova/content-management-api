package com.leman.contentmanagementapi.service;

import static com.leman.contentmanagementapi.constant.CategoryTestConstant.CATEGORY_CREATE_REQUEST;
import static com.leman.contentmanagementapi.constant.CategoryTestConstant.CATEGORY_RESPONSE;
import static com.leman.contentmanagementapi.constant.CategoryTestConstant.CATEGORY_STATUS_CHANGE_REQUEST;
import static com.leman.contentmanagementapi.constant.CategoryTestConstant.CATEGORY_UPDATE_REQUEST;
import static com.leman.contentmanagementapi.constant.CategoryTestConstant.categoryEntity;
import static com.leman.contentmanagementapi.constant.TestConstant.ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import com.leman.contentmanagementapi.dto.response.CategoryResponse;
import com.leman.contentmanagementapi.entity.Category;
import com.leman.contentmanagementapi.exception.DuplicateResourceException;
import com.leman.contentmanagementapi.exception.ResourceNotFoundException;
import com.leman.contentmanagementapi.mapper.CategoryMapper;
import com.leman.contentmanagementapi.repository.CategoryRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Spy
    private CategoryMapper categoryMapper = Mappers.getMapper(CategoryMapper.class);

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void createCategory_Should_Return_Success() {
        Category categoryEntity = categoryEntity();
        given(categoryRepository.existsByName(CATEGORY_CREATE_REQUEST.getName())).willReturn(false);
        given(categoryRepository.save(any(Category.class))).willReturn(categoryEntity);

        CategoryResponse result = categoryService.createCategory(CATEGORY_CREATE_REQUEST);
        assertNotNull(result);
        assertEquals(CATEGORY_RESPONSE, result);

        then(categoryRepository).should(times(1)).existsByName(CATEGORY_CREATE_REQUEST.getName());
        then(categoryRepository).should(times(1)).save(any(Category.class));
    }

    @Test
    void createCategory_Should_Throw_DuplicateResourceException() {
        given(categoryRepository.existsByName(CATEGORY_CREATE_REQUEST.getName())).willReturn(true);

        assertThrows(DuplicateResourceException.class, () -> categoryService.createCategory(CATEGORY_CREATE_REQUEST));

        then(categoryRepository).should(times(1)).existsByName(CATEGORY_CREATE_REQUEST.getName());
        then(categoryRepository).should(never()).save(any());
    }

    @Test
    void findAllCategories_Should_Return_Success() {
        Category categoryEntity = categoryEntity();
        given(categoryRepository.findAllByActiveTrue()).willReturn(List.of(categoryEntity));

        List<CategoryResponse> result = categoryService.findAllCategories();
        assertNotNull(result);
        assertEquals(List.of(CATEGORY_RESPONSE), result);

        then(categoryRepository).should(times(1)).findAllByActiveTrue();
    }

    @Test
    void findCategoryById_Should_Return_Success() {
        Category categoryEntity = categoryEntity();
        given(categoryRepository.findById(ID)).willReturn(Optional.of(categoryEntity));

        CategoryResponse result = categoryService.findCategoryById(ID);

        assertNotNull(result);
        assertEquals(CATEGORY_RESPONSE, result);
        then(categoryRepository).should(times(1)).findById(ID);
    }

    @Test
    void findCategoryById_Should_Throw_ResourceNotFoundException() {
        given(categoryRepository.findById(ID)).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.findCategoryById(ID));

        then(categoryRepository).should(times(1)).findById(ID);
    }

    @Test
    void findActiveCategoryById_Should_Return_Success() {
        Category categoryEntity = categoryEntity();
        given(categoryRepository.findByIdAndActiveTrue(ID)).willReturn(Optional.of(categoryEntity));

        CategoryResponse result = categoryService.findActiveCategoryById(ID);
        assertNotNull(result);
        assertEquals(CATEGORY_RESPONSE, result);

        then(categoryRepository).should(times(1)).findByIdAndActiveTrue(ID);
    }

    @Test
    void findActiveCategoryById_Should_Throw_ResourceNotFoundException() {
        given(categoryRepository.findByIdAndActiveTrue(ID)).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.findActiveCategoryById(ID));

        then(categoryRepository).should(times(1)).findByIdAndActiveTrue(ID);
    }

    @Test
    void updateCategory_Should_Return_Success() {
        Category categoryEntity = categoryEntity();
        given(categoryRepository.findByIdAndActiveTrue(ID)).willReturn(Optional.of(categoryEntity));
        given(categoryRepository.existsByNameAndIdNot(CATEGORY_UPDATE_REQUEST.getName(), ID)).willReturn(false);

        CategoryResponse result = categoryService.updateCategory(ID, CATEGORY_UPDATE_REQUEST);

        assertNotNull(result);
        assertEquals(CATEGORY_RESPONSE, result);
        then(categoryRepository).should(times(1)).findByIdAndActiveTrue(ID);
        then(categoryRepository).should(times(1)).existsByNameAndIdNot(CATEGORY_UPDATE_REQUEST.getName(), ID);
    }

    @Test
    void updateCategory_Should_Throw_DuplicateResourceException() {
        Category categoryEntity = categoryEntity();
        given(categoryRepository.findByIdAndActiveTrue(ID)).willReturn(Optional.of(categoryEntity));
        given(categoryRepository.existsByNameAndIdNot(CATEGORY_UPDATE_REQUEST.getName(), ID)).willReturn(true);

        assertThrows(DuplicateResourceException.class,
                () -> categoryService.updateCategory(ID, CATEGORY_UPDATE_REQUEST));

        then(categoryRepository).should(times(1)).findByIdAndActiveTrue(ID);
        then(categoryRepository).should(times(1)).existsByNameAndIdNot(CATEGORY_UPDATE_REQUEST.getName(), ID);
        then(categoryRepository).should(never()).save(any());
    }

    @Test
    void updateCategory_Should_Throw_ResourceNotFoundException() {
        given(categoryRepository.findByIdAndActiveTrue(ID)).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> categoryService.updateCategory(ID, CATEGORY_UPDATE_REQUEST));

        then(categoryRepository).should(times(1)).findByIdAndActiveTrue(ID);
        then(categoryRepository).should(never()).existsByNameAndIdNot(any(), any());
    }

    @Test
    void updateCategoryStatus_Should_Return_Success() {
        Category categoryEntity = categoryEntity();
        given(categoryRepository.findById(ID)).willReturn(Optional.of(categoryEntity));

        categoryService.updateCategoryStatus(ID, CATEGORY_STATUS_CHANGE_REQUEST);

        assertTrue(categoryEntity.getActive());

        then(categoryRepository).should(times(1)).findById(ID);
        then(categoryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void updateCategoryStatus_Should_Throw_ResourceNotFoundException() {
        given(categoryRepository.findById(ID)).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> categoryService.updateCategoryStatus(ID, CATEGORY_STATUS_CHANGE_REQUEST));

        then(categoryRepository).should(times(1)).findById(ID);
    }

}
