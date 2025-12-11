package com.leman.contentmanagementapi.service;

import com.leman.contentmanagementapi.dto.request.CategoryCreateRequest;
import com.leman.contentmanagementapi.dto.response.CategoryResponse;
import com.leman.contentmanagementapi.entity.Category;
import com.leman.contentmanagementapi.exception.ResourceNotFoundException;
import com.leman.contentmanagementapi.mapper.CategoryMapper;
import com.leman.contentmanagementapi.repository.CategoryRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;

    public CategoryResponse createCategory(@Valid CategoryCreateRequest request) {
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

}
