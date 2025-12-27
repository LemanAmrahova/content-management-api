package com.leman.contentmanagementapi.service;

import com.leman.contentmanagementapi.dto.request.CategoryCreateRequest;
import com.leman.contentmanagementapi.dto.request.CategoryStatusChangeRequest;
import com.leman.contentmanagementapi.dto.request.CategoryUpdateRequest;
import com.leman.contentmanagementapi.dto.response.CategoryResponse;
import com.leman.contentmanagementapi.entity.Category;
import com.leman.contentmanagementapi.exception.DuplicateResourceException;
import com.leman.contentmanagementapi.exception.ResourceNotFoundException;
import com.leman.contentmanagementapi.mapper.CategoryMapper;
import com.leman.contentmanagementapi.repository.CategoryRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryResponse createCategory(@Valid CategoryCreateRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Category", "name", request.getName());
        }

        Category saved = categoryRepository.save(categoryMapper.toEntity(request));
        log.info("Category created successfully with ID: {}", saved.getId());

        return categoryMapper.toResponse(saved);
    }

    public List<CategoryResponse> findAllCategories() {
        return categoryMapper.toResponse(categoryRepository.findAllByActiveTrue());
    }

    public CategoryResponse findCategoryById(Long id) {
        return categoryRepository.findByIdAndActiveTrue(id).map(categoryMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
    }

    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryUpdateRequest request) {
        Category category =  categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        category.setName(request.getName());
        log.info("Category updated successfully with ID: {}", id);

        return categoryMapper.toResponse(category);
    }

    @Transactional
    public void changeCategoryStatus(Long id, CategoryStatusChangeRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        category.setActive(request.getActive());
        log.info("Category status changed successfully with ID: {}", id);
    }

}
