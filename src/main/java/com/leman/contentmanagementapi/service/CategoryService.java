package com.leman.contentmanagementapi.service;

import static com.leman.contentmanagementapi.constant.ApplicationConstant.Common.ID;
import static com.leman.contentmanagementapi.constant.ApplicationConstant.Common.NAME;

import com.leman.contentmanagementapi.dto.request.CategoryCreateRequest;
import com.leman.contentmanagementapi.dto.request.CategoryStatusChangeRequest;
import com.leman.contentmanagementapi.dto.request.CategoryUpdateRequest;
import com.leman.contentmanagementapi.dto.response.CategoryResponse;
import com.leman.contentmanagementapi.entity.Category;
import com.leman.contentmanagementapi.exception.DuplicateResourceException;
import com.leman.contentmanagementapi.exception.ResourceNotFoundException;
import com.leman.contentmanagementapi.mapper.CategoryMapper;
import com.leman.contentmanagementapi.repository.CategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private static final String ENTITY = "Category";

    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryResponse createCategory(CategoryCreateRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw DuplicateResourceException.of(ENTITY, NAME, request.getName());
        }

        Category saved = categoryRepository.save(categoryMapper.toEntity(request));
        log.info("Category created successfully with ID: {}", saved.getId());

        return categoryMapper.toResponse(saved);
    }

    public List<CategoryResponse> findAllCategories() {
        return categoryMapper.toResponse(categoryRepository.findAllByActiveTrue());
    }

    public CategoryResponse findCategoryById(Long id) {
        Category category = findExistingCategoryById(id);

        return categoryMapper.toResponse(category);
    }

    public CategoryResponse findActiveCategoryById(Long id) {
        Category category = categoryRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> ResourceNotFoundException.of(ENTITY, ID, id));

        return categoryMapper.toResponse(category);
    }

    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryUpdateRequest request) {
        Category category = categoryRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> ResourceNotFoundException.of(ENTITY, ID, id));

        if (categoryRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw DuplicateResourceException.of(ENTITY, NAME, request.getName());
        }

        category.setName(request.getName());
        log.info("Category updated successfully with ID: {}", id);

        return categoryMapper.toResponse(category);
    }

    @Transactional
    public void changeCategoryStatus(Long id, CategoryStatusChangeRequest request) {
        Category category = findExistingCategoryById(id);

        category.setActive(request.getActive());
        log.info("Category status changed successfully with ID: {}", id);
    }

    private Category findExistingCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of(ENTITY, ID, id));
    }

}
