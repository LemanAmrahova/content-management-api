package com.leman.contentmanagementapi.service;

import com.leman.contentmanagementapi.dto.request.ArticleCreateRequest;
import com.leman.contentmanagementapi.dto.response.ArticleResponse;
import com.leman.contentmanagementapi.entity.Article;
import com.leman.contentmanagementapi.exception.ResourceNotFoundException;
import com.leman.contentmanagementapi.mapper.ArticleMapper;
import com.leman.contentmanagementapi.repository.ArticleRepository;
import com.leman.contentmanagementapi.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {

    private final ArticleMapper articleMapper;
    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public ArticleResponse createArticle(ArticleCreateRequest request) {
        var category = categoryRepository.findByIdAndActiveTrue(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));

        Article entity = articleMapper.toEntity(request);
        entity.setCategory(category);
        Article saved = articleRepository.save(entity);
        log.info("Article created successfully with ID: {}", saved.getId());

        return articleMapper.toResponse(saved);
    }

}
