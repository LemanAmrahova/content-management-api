package com.leman.contentmanagementapi.service;

import com.leman.contentmanagementapi.dto.response.ArticleResponse;
import com.leman.contentmanagementapi.mapper.ArticleMapper;
import com.leman.contentmanagementapi.repository.ArticleRepository;
import com.leman.contentmanagementapi.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static com.leman.contentmanagementapi.constant.ArticleTestConstant.ARTICLE_CREATE_REQUEST;
import static com.leman.contentmanagementapi.constant.ArticleTestConstant.ARTICLE_ENTITY;
import static com.leman.contentmanagementapi.constant.ArticleTestConstant.ARTICLE_RESPONSE;
import static com.leman.contentmanagementapi.constant.CategoryTestConstant.CATEGORY_ENTITY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

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
    void createArticle_ShouldReturn_Success() throws Exception {
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

}
