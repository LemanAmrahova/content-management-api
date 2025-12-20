package com.leman.contentmanagementapi.service;

import com.leman.contentmanagementapi.dto.request.CategoryCreateRequest;
import com.leman.contentmanagementapi.dto.request.CategoryUpdateRequest;
import com.leman.contentmanagementapi.dto.response.CategoryResponse;
import com.leman.contentmanagementapi.entity.Category;
import com.leman.contentmanagementapi.exception.DuplicateResourceException;
import com.leman.contentmanagementapi.exception.ResourceNotFoundException;
import com.leman.contentmanagementapi.mapper.CategoryMapper;
import com.leman.contentmanagementapi.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;

    public CategoryResponse createCategory(@Valid CategoryCreateRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Category", "name", request.getName());
        }

        Category entity = categoryMapper.toEntity(request);
        Category saved = categoryRepository.save(entity);

        log.info("Category created successfully with ID: {}", saved.getId());
        return categoryMapper.toResponse(saved);
    }

    public List<CategoryResponse> findAllCategories() {
        return categoryMapper.toResponse(categoryRepository.findAll());
    }

    public CategoryResponse findCategoryById(Long id) {
        return categoryRepository.findById(id).map(categoryMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
    }

    public CategoryResponse updateCategory(Long id, CategoryUpdateRequest request) {
        Category category =  categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        category.setName(request.getName());
        Category updated = categoryRepository.save(category);

        log.info("Category updated successfully with ID: {}", id);
        return categoryMapper.toResponse(updated);
    }

    @Transactional
    public void deleteCategory(Long id) {
        categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        categoryRepository.deactivateById(id);
        log.info("Category soft deleted successfully with ID: {}", id);
    }

}
