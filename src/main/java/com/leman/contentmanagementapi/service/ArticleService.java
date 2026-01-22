package com.leman.contentmanagementapi.service;

import com.leman.contentmanagementapi.dto.request.ArticleCreateRequest;
import com.leman.contentmanagementapi.dto.request.ArticleFilterRequest;
import com.leman.contentmanagementapi.dto.request.ArticleUpdateRequest;
import com.leman.contentmanagementapi.dto.response.ArticleDetailResponse;
import com.leman.contentmanagementapi.dto.response.ArticleResponse;
import com.leman.contentmanagementapi.dto.response.PageableResponse;
import com.leman.contentmanagementapi.entity.Article;
import com.leman.contentmanagementapi.entity.Category;
import com.leman.contentmanagementapi.exception.ResourceNotFoundException;
import com.leman.contentmanagementapi.mapper.ArticleMapper;
import com.leman.contentmanagementapi.repository.ArticleRepository;
import com.leman.contentmanagementapi.repository.CategoryRepository;
import com.leman.contentmanagementapi.specification.ArticleSpecification;
import com.leman.contentmanagementapi.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
        Category category = categoryRepository.findByIdAndActiveTrue(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));

        Article entity = articleMapper.toEntity(request);
        entity.setCategory(category);
        Article saved = articleRepository.save(entity);
        log.info("Article created successfully with ID: {}", saved.getId());

        return articleMapper.toResponse(saved);
    }

    public PageableResponse<ArticleDetailResponse> findAllArticles(ArticleFilterRequest request) {
        Pageable pageable = PaginationUtil.createPageable(request);
        Specification<Article> articleSpecification = ArticleSpecification.getSpecification(request);

        return articleMapper.toResponse(articleRepository.findAll(articleSpecification, pageable));
    }

    public ArticleDetailResponse findArticleById(Long id) {
        return articleRepository.findByIdAndActiveWithCategory(id).map(articleMapper::toDetailResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Article", "id", id));
    }

    @Transactional
    public ArticleResponse updateArticle(Long id, ArticleUpdateRequest request) {
        Article article = articleRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article", "id", id));

        Category category = categoryRepository.findByIdAndActiveTrue(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));

        article.setTitle(request.getTitle());
        article.setContent(request.getContent());
        article.setCategory(category);
        log.info("Article updated successfully with ID: {}", id);

        return articleMapper.toResponse(article);
    }

    @Transactional
    public void publishArticle(Long id) {
        Article article = articleRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article", "id", id));

        article.setPublished(true);
        log.info("Article published successfully with ID: {}", id);
    }

    @Transactional
    public void deleteArticle(Long id) {
        Article article = articleRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article", "id", id));

        article.setActive(false);
        log.info("Article deleted successfully with ID: {}", id);
    }

}
