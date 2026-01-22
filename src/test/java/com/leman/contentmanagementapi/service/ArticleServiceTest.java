package com.leman.contentmanagementapi.service;

import com.leman.contentmanagementapi.dto.response.ArticleDetailResponse;
import com.leman.contentmanagementapi.dto.response.ArticleResponse;
import com.leman.contentmanagementapi.dto.response.PageableResponse;
import com.leman.contentmanagementapi.entity.Article;
import com.leman.contentmanagementapi.mapper.ArticleMapper;
import com.leman.contentmanagementapi.projection.ArticleDetailProjection;
import com.leman.contentmanagementapi.repository.ArticleRepository;
import com.leman.contentmanagementapi.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import java.util.Optional;

import static com.leman.contentmanagementapi.constant.ArticleTestConstant.ARTICLE_CREATE_REQUEST;
import static com.leman.contentmanagementapi.constant.ArticleTestConstant.ARTICLE_DETAIL_RESPONSE;
import static com.leman.contentmanagementapi.constant.ArticleTestConstant.ARTICLE_ENTITY;
import static com.leman.contentmanagementapi.constant.ArticleTestConstant.ARTICLE_FILTER_REQUEST;
import static com.leman.contentmanagementapi.constant.ArticleTestConstant.ARTICLE_RESPONSE;
import static com.leman.contentmanagementapi.constant.ArticleTestConstant.ARTICLE_UPDATE_REQUEST;
import static com.leman.contentmanagementapi.constant.ArticleTestConstant.PAGEABLE_ARTICLE_RESPONSE;
import static com.leman.contentmanagementapi.constant.CategoryTestConstant.CATEGORY_ENTITY;
import static com.leman.contentmanagementapi.constant.TestConstant.ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceTest {

    @Mock
    ArticleMapper articleMapper;

    @Mock
    ArticleRepository articleRepository;

    @Mock
    CategoryRepository categoryRepository;

    @InjectMocks
    ArticleService articleService;

    @Test
    void createArticle_ShouldReturn_Success() {
        given(categoryRepository.findByIdAndActiveTrue(ARTICLE_CREATE_REQUEST.getCategoryId()))
                .willReturn(Optional.of(CATEGORY_ENTITY));
        given(articleMapper.toEntity(ARTICLE_CREATE_REQUEST)).willReturn(ARTICLE_ENTITY);
        given(articleRepository.save(ARTICLE_ENTITY)).willReturn(ARTICLE_ENTITY);
        given(articleMapper.toResponse(ARTICLE_ENTITY)).willReturn(ARTICLE_RESPONSE);

        ArticleResponse result = articleService.createArticle(ARTICLE_CREATE_REQUEST);
        assertThat(result).isEqualTo(ARTICLE_RESPONSE);

        then(categoryRepository).should().findByIdAndActiveTrue(ARTICLE_CREATE_REQUEST.getCategoryId());
        then(articleMapper).should().toEntity(ARTICLE_CREATE_REQUEST);
        then(articleRepository).should().save(ARTICLE_ENTITY);
        then(articleMapper).should().toResponse(ARTICLE_ENTITY);
    }

    @Test
    void findAllArticles_ShouldReturn_Success() {
        Page<Article> articlePage = mock(Page.class);

        given(articleRepository.findAll(any(Specification.class), any(Pageable.class))).willReturn(articlePage);
        given(articleMapper.toResponse(articlePage)).willReturn(PAGEABLE_ARTICLE_RESPONSE);

        PageableResponse<ArticleDetailResponse> result = articleService.findAllArticles(ARTICLE_FILTER_REQUEST);
        assertThat(result).isEqualTo(PAGEABLE_ARTICLE_RESPONSE);

        then(articleRepository).should().findAll(any(Specification.class), any(Pageable.class));
        then(articleMapper).should().toResponse(articlePage);
    }

    @Test
    void findArticleById_ShouldReturn_Success() {
        ArticleDetailProjection projection = mock(ArticleDetailProjection.class);

        given(articleRepository.findByIdAndActiveWithCategory(ID)).willReturn(Optional.of(projection));
        given(articleMapper.toDetailResponse(projection)).willReturn(ARTICLE_DETAIL_RESPONSE);

        assertThat(articleService.findArticleById(ID)).isEqualTo(ARTICLE_DETAIL_RESPONSE);

        then(articleRepository).should().findByIdAndActiveWithCategory(ID);
    }

    @Test
    void updateArticle_ShouldReturn_Success() {
        given(articleRepository.findByIdAndActiveTrue(ID)).willReturn(Optional.of(ARTICLE_ENTITY));
        given(categoryRepository.findByIdAndActiveTrue(ARTICLE_UPDATE_REQUEST.getCategoryId()))
                .willReturn(Optional.of(CATEGORY_ENTITY));
        given(articleMapper.toResponse(ARTICLE_ENTITY)).willReturn(ARTICLE_RESPONSE);

        assertThat(articleService.updateArticle(ID, ARTICLE_UPDATE_REQUEST)).isEqualTo(ARTICLE_RESPONSE);
        assertThat(ARTICLE_ENTITY.getTitle()).isEqualTo(ARTICLE_UPDATE_REQUEST.getTitle());
        assertThat(ARTICLE_ENTITY.getContent()).isEqualTo(ARTICLE_UPDATE_REQUEST.getContent());
        assertThat(ARTICLE_ENTITY.getCategory()).isEqualTo(CATEGORY_ENTITY);

        then(articleRepository).should().findByIdAndActiveTrue(ID);
        then(categoryRepository).should().findByIdAndActiveTrue(ARTICLE_CREATE_REQUEST.getCategoryId());
        then(articleMapper).should().toResponse(ARTICLE_ENTITY);
    }

    @Test
    void publishArticle_ShouldReturn_Success() {
        given(articleRepository.findByIdAndActiveTrue(ID)).willReturn(Optional.of(ARTICLE_ENTITY));

        articleService.publishArticle(ID);
        assertThat(ARTICLE_ENTITY.getPublished()).isEqualTo(true);

        then(articleRepository).should().findByIdAndActiveTrue(ID);
        then(articleRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void deleteArticle_ShouldReturn_Success() {
        given(articleRepository.findByIdAndActiveTrue(ID)).willReturn(Optional.of(ARTICLE_ENTITY));

        articleService.deleteArticle(ID);
        assertThat(ARTICLE_ENTITY.getActive()).isEqualTo(false);

        then(articleRepository).should().findByIdAndActiveTrue(ID);
        then(articleRepository).shouldHaveNoMoreInteractions();
    }

}
