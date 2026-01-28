package com.leman.contentmanagementapi.controller;

import com.leman.contentmanagementapi.annotation.ExcludeSecurityWebMvcTest;
import com.leman.contentmanagementapi.dto.request.ArticleCreateRequest;
import com.leman.contentmanagementapi.dto.request.ArticleFilterRequest;
import com.leman.contentmanagementapi.dto.request.ArticleUpdateRequest;
import com.leman.contentmanagementapi.dto.response.ArticleDetailResponse;
import com.leman.contentmanagementapi.dto.response.ArticleResponse;
import com.leman.contentmanagementapi.dto.response.PageableResponse;
import com.leman.contentmanagementapi.service.ArticleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.leman.contentmanagementapi.constant.ArticleTestConstant.ARTICLE_CREATE_REQUEST;
import static com.leman.contentmanagementapi.constant.ArticleTestConstant.ARTICLE_DETAIL_RESPONSE;
import static com.leman.contentmanagementapi.constant.ArticleTestConstant.ARTICLE_FILTER_REQUEST;
import static com.leman.contentmanagementapi.constant.ArticleTestConstant.ARTICLE_RESPONSE;
import static com.leman.contentmanagementapi.constant.ArticleTestConstant.ARTICLE_UPDATE_REQUEST;
import static com.leman.contentmanagementapi.constant.ArticleTestConstant.PAGEABLE_ARTICLE_RESPONSE;
import static com.leman.contentmanagementapi.constant.TestConstant.ID;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExcludeSecurityWebMvcTest(controllers = ArticleController.class)
@AutoConfigureJsonTesters
public class ArticleControllerTest {

    private static final String BASE_PATH = "/api/v1/articles";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ArticleService articleService;

    @Autowired
    private JacksonTester<ArticleCreateRequest> createTester;

    @Autowired
    private JacksonTester<ArticleFilterRequest> filterTester;

    @Autowired
    private JacksonTester<ArticleUpdateRequest> updateTester;

    @Autowired
    private JacksonTester<ArticleResponse> responseTester;

    @Autowired
    private JacksonTester<ArticleDetailResponse> detailResponseTester;

    @Autowired
    private JacksonTester<PageableResponse<ArticleDetailResponse>> pageableDetailResponseTester;

    @Test
    void create_ShouldReturn_Success() throws Exception {
        given(articleService.createArticle(ARTICLE_CREATE_REQUEST)).willReturn(ARTICLE_RESPONSE);

        mockMvc.perform(post(BASE_PATH)
                        .content(createTester.write(ARTICLE_CREATE_REQUEST).getJson())
                        .contentType("application/json"))
                .andExpect(status().isCreated())
                .andExpect(content().json(responseTester.write(ARTICLE_RESPONSE).getJson()));
    }

    @Test
    void getAll_ShouldReturn_Success() throws Exception {
        given(articleService.findAllArticles(ARTICLE_FILTER_REQUEST)).willReturn(PAGEABLE_ARTICLE_RESPONSE);

        mockMvc.perform(get(BASE_PATH)
                        .param("page", String.valueOf(ARTICLE_FILTER_REQUEST.getPage()))
                        .param("size", String.valueOf(ARTICLE_FILTER_REQUEST.getSize()))
                        .param("sortBy", ARTICLE_FILTER_REQUEST.getSortBy())
                        .param("sortDirection", ARTICLE_FILTER_REQUEST.getSortDirection().name())
                        .param("title", ARTICLE_FILTER_REQUEST.getTitle())
                        .param("content", ARTICLE_FILTER_REQUEST.getContent())
                        .param("categoryId", ARTICLE_FILTER_REQUEST.getCategoryId().toString())
                        .param("published", ARTICLE_FILTER_REQUEST.getPublished().toString())
                        .param("active", ARTICLE_FILTER_REQUEST.getActive().toString()))
                .andExpect(status().isOk())
                .andExpect(content().json(pageableDetailResponseTester.write(PAGEABLE_ARTICLE_RESPONSE).getJson()));
    }

    @Test
    void getById_ShouldReturn_Success() throws Exception {
        given(articleService.findArticleById(ID)).willReturn(ARTICLE_DETAIL_RESPONSE);

        mockMvc.perform(get(BASE_PATH + "/" + ID)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(detailResponseTester.write(ARTICLE_DETAIL_RESPONSE).getJson()));
    }

    @Test
    void update_ShouldReturn_Success() throws Exception {
        given(articleService.updateArticle(ID, ARTICLE_UPDATE_REQUEST)).willReturn(ARTICLE_RESPONSE);

        mockMvc.perform(put(BASE_PATH + "/" + ID)
                        .content(updateTester.write(ARTICLE_UPDATE_REQUEST).getJson())
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(responseTester.write(ARTICLE_RESPONSE).getJson()));
    }

    @Test
    void publish_ShouldReturn_Success() throws Exception {
        willDoNothing().given(articleService).publishArticle(ID);

        mockMvc.perform(patch(BASE_PATH + "/" + ID + "/publish")
                        .content("application/json"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

    @Test
    void delete_ShouldReturn_Success() throws Exception {
        willDoNothing().given(articleService).deleteArticle(ID);

        mockMvc.perform(delete(BASE_PATH + "/" + ID)
                        .content("application/json"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

}
