package com.leman.contentmanagementapi.controller;

import static org.springframework.http.HttpStatus.CREATED;

import com.leman.contentmanagementapi.dto.request.CategoryCreateRequest;
import com.leman.contentmanagementapi.dto.request.CategoryStatusChangeRequest;
import com.leman.contentmanagementapi.dto.request.CategoryUpdateRequest;
import com.leman.contentmanagementapi.dto.response.CategoryResponse;
import com.leman.contentmanagementapi.service.CategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CategoryResponse> create(@RequestBody @Valid CategoryCreateRequest request) {
        return ResponseEntity.status(CREATED).body(categoryService.createCategory(request));
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAll() {
        return ResponseEntity.ok(categoryService.findAllCategories());
    }

    @GetMapping("{id}")
    public ResponseEntity<CategoryResponse> getById(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(categoryService.findCategoryById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity<CategoryResponse> update(@PathVariable @Positive Long id,
                                                   @RequestBody @Valid CategoryUpdateRequest request) {
        return ResponseEntity.ok(categoryService.updateCategory(id, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("{id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable @Positive Long id,
                                             @RequestBody @Valid CategoryStatusChangeRequest request) {
        categoryService.updateCategoryStatus(id, request);

        return ResponseEntity.noContent().build();
    }

}
