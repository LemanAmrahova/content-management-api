package com.leman.contentmanagementapi.service;

import static com.leman.contentmanagementapi.constant.ArticleTestConstant.ARTICLE_CREATE_REQUEST;
import static com.leman.contentmanagementapi.constant.ArticleTestConstant.ARTICLE_FILTER_REQUEST;
import static com.leman.contentmanagementapi.constant.ArticleTestConstant.ARTICLE_RESPONSE;
import static com.leman.contentmanagementapi.constant.ArticleTestConstant.ARTICLE_UPDATE_REQUEST;
import static com.leman.contentmanagementapi.constant.ArticleTestConstant.articleEntity;
import static com.leman.contentmanagementapi.constant.CategoryTestConstant.CATEGORY_RESPONSE;
import static com.leman.contentmanagementapi.constant.TestConstant.ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import com.leman.contentmanagementapi.dto.response.ArticleResponse;
import com.leman.contentmanagementapi.dto.response.PageableResponse;
import com.leman.contentmanagementapi.entity.Article;
import com.leman.contentmanagementapi.mapper.ArticleMapper;
import com.leman.contentmanagementapi.repository.ArticleRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @Spy
    private ArticleMapper articleMapper = Mappers.getMapper(ArticleMapper.class);

    @Mock
    ArticleRepository articleRepository;

    @Mock
    CategoryService categoryService;

    @InjectMocks
    ArticleService articleService;

    @Test
    void createArticle_ShouldReturn_Success() {
        Article articleEntity = articleEntity();

        given(categoryService.findActiveCategoryById(ARTICLE_CREATE_REQUEST.getCategoryId()))
                .willReturn(CATEGORY_RESPONSE);
        given(articleRepository.save(any(Article.class))).willReturn(articleEntity);

        ArticleResponse result = articleService.createArticle(ARTICLE_CREATE_REQUEST);
        assertThat(result).isEqualTo(ARTICLE_RESPONSE);

        then(categoryService).should().findActiveCategoryById(ARTICLE_CREATE_REQUEST.getCategoryId());
        then(articleRepository).should().save(any(Article.class));
    }

    @Test
    void findAllArticles_ShouldReturn_Success() {
        Article articleEntity = articleEntity();

        Page<Article> articlePage = mock(Page.class);
        given(articlePage.getContent()).willReturn(List.of(articleEntity));
        given(articleRepository.findAll(any(Specification.class), any(Pageable.class))).willReturn(articlePage);

        PageableResponse<ArticleResponse> result = articleService.findAllArticles(ARTICLE_FILTER_REQUEST);
        assertThat(result).isNotNull();

        then(articleRepository).should().findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findArticleById_ShouldReturn_Success() {
        Article articleEntity = articleEntity();

        given(articleRepository.findByIdAndActiveTrue(ID)).willReturn(Optional.of(articleEntity));

        ArticleResponse result = articleService.findArticleById(ID);
        assertThat(result).isNotNull();

        then(articleRepository).should().findByIdAndActiveTrue(ID);
    }

    @Test
    void updateArticle_ShouldReturn_Success() {
        Article articleEntity = articleEntity();

        given(articleRepository.findByIdAndActiveTrue(ID)).willReturn(Optional.of(articleEntity));
        given(categoryService.findActiveCategoryById(ARTICLE_UPDATE_REQUEST.getCategoryId()))
                .willReturn(CATEGORY_RESPONSE);

        ArticleResponse result = articleService.updateArticle(ID, ARTICLE_UPDATE_REQUEST);
        assertThat(result).isNotNull();
        assertEquals(ARTICLE_RESPONSE, result);

        then(articleRepository).should().findByIdAndActiveTrue(ID);
        then(categoryService).should().findActiveCategoryById(ARTICLE_UPDATE_REQUEST.getCategoryId());
    }

    @Test
    void publishArticle_ShouldReturn_Success() {
        Article articleEntity = articleEntity();

        given(articleRepository.findByIdAndActiveTrue(ID)).willReturn(Optional.of(articleEntity));

        articleService.publishArticle(ID);
        assertThat(articleEntity.getPublished()).isTrue();

        then(articleRepository).should().findByIdAndActiveTrue(ID);
        then(articleRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void deleteArticle_ShouldReturn_Success() {
        Article articleEntity = articleEntity();

        given(articleRepository.findByIdAndActiveTrue(ID)).willReturn(Optional.of(articleEntity));

        articleService.deleteArticle(ID);
        assertThat(articleEntity.getActive()).isFalse();

        then(articleRepository).should().findByIdAndActiveTrue(ID);
        then(articleRepository).shouldHaveNoMoreInteractions();
    }

}
