package com.leman.contentmanagementapi.controller;

import com.leman.contentmanagementapi.dto.request.ArticleCreateRequest;
import com.leman.contentmanagementapi.dto.request.ArticleUpdateRequest;
import com.leman.contentmanagementapi.dto.response.ArticleDetailResponse;
import com.leman.contentmanagementapi.dto.response.ArticleResponse;
import com.leman.contentmanagementapi.service.ArticleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.leman.contentmanagementapi.constant.ArticleTestConstant.ARTICLE_CREATE_REQUEST;
import static com.leman.contentmanagementapi.constant.ArticleTestConstant.ARTICLE_DETAIL_RESPONSE;
import static com.leman.contentmanagementapi.constant.ArticleTestConstant.ARTICLE_RESPONSE;
import static com.leman.contentmanagementapi.constant.ArticleTestConstant.ARTICLE_UPDATE_REQUEST;
import static com.leman.contentmanagementapi.constant.TestConstant.ID;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureJsonTesters
@WebMvcTest(ArticleController.class)
public class ArticleControllerTest {

    private static final String BASE_PATH = "/api/v1/articles";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ArticleService articleService;

    @Autowired
    private JacksonTester<ArticleCreateRequest> createTester;

    @Autowired
    private JacksonTester<ArticleUpdateRequest> updateTester;

    @Autowired
    private JacksonTester<ArticleResponse> responseTester;

    @Autowired
    private JacksonTester<ArticleDetailResponse> detailResponseTester;

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
    void getById_ShouldReturn_Success() throws Exception {
        given(articleService.findArticleById(ID)).willReturn(ARTICLE_DETAIL_RESPONSE);

        mockMvc.perform(get(BASE_PATH + "/" + ID)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(detailResponseTester.write(ARTICLE_DETAIL_RESPONSE).getJson()));
    }

    @Test
    void update_shouldReturn_Success() throws Exception {
        given(articleService.updateArticle(ID, ARTICLE_UPDATE_REQUEST)).willReturn(ARTICLE_RESPONSE);

        mockMvc.perform(put(BASE_PATH + "/" + ID)
                        .content(updateTester.write(ARTICLE_UPDATE_REQUEST).getJson())
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(responseTester.write(ARTICLE_RESPONSE).getJson()));
    }

    @Test
    void delete_shouldReturn_Success() throws Exception {
        willDoNothing().given(articleService).deleteArticle(ID);

        mockMvc.perform(delete(BASE_PATH + "/" + ID)
                        .content("application/json"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

}
