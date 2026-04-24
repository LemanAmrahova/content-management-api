package com.leman.contentmanagementapi.service;

import static com.leman.contentmanagementapi.constant.ApplicationConstant.Common.ID;

import com.leman.contentmanagementapi.dto.request.ArticleCreateRequest;
import com.leman.contentmanagementapi.dto.request.ArticleFilterRequest;
import com.leman.contentmanagementapi.dto.request.ArticleUpdateRequest;
import com.leman.contentmanagementapi.dto.response.ArticleResponse;
import com.leman.contentmanagementapi.dto.response.PageableResponse;
import com.leman.contentmanagementapi.entity.Article;
import com.leman.contentmanagementapi.exception.ResourceNotFoundException;
import com.leman.contentmanagementapi.mapper.ArticleMapper;
import com.leman.contentmanagementapi.repository.ArticleRepository;
import com.leman.contentmanagementapi.specification.ArticleSpecification;
import com.leman.contentmanagementapi.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {

    private static final String ENTITY = "Article";

    private final ArticleMapper articleMapper;
    private final ArticleRepository articleRepository;
    private final CategoryService categoryService;

    @Transactional
    public ArticleResponse createArticle(ArticleCreateRequest request) {
        categoryService.findActiveCategoryById(request.getCategoryId());

        Article saved = articleRepository.save(articleMapper.toEntity(request));
        log.info("Article created successfully with ID: {}", saved.getId());

        return articleMapper.toResponse(saved);
    }

    public PageableResponse<ArticleResponse> findAllArticles(ArticleFilterRequest request) {
        Pageable pageable = PaginationUtil.createPageable(request);
        Specification<Article> articleSpecification = ArticleSpecification.getSpecification(request);

        return articleMapper.toPageableResponse(articleRepository.findAll(articleSpecification, pageable));
    }

    public ArticleResponse findArticleById(Long id) {
        Article article = findActiveArticleById(id);

        return articleMapper.toDetailResponse(article);
    }

    @Transactional
    public ArticleResponse updateArticle(Long id, ArticleUpdateRequest request) {
        Article article = findActiveArticleById(id);

        categoryService.findActiveCategoryById(request.getCategoryId());

        article.setTitle(request.getTitle());
        article.setContent(request.getContent());
        article.getCategory().setId(request.getCategoryId());
        log.info("Article updated successfully with ID: {}", id);

        return articleMapper.toResponse(article);
    }

    @Transactional
    public void publishArticle(Long id) {
        Article article = findActiveArticleById(id);

        article.setPublished(true);
        log.info("Article published successfully with ID: {}", id);
    }

    @Transactional
    public void deleteArticle(Long id) {
        Article article = findActiveArticleById(id);

        article.setActive(false);
        log.info("Article deleted successfully with ID: {}", id);
    }

    private Article findActiveArticleById(Long id) {
        return articleRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> ResourceNotFoundException.of(ENTITY, ID, id));
    }

}
