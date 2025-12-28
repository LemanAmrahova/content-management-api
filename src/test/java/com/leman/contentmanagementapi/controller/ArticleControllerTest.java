package com.leman.contentmanagementapi.controller;

import com.leman.contentmanagementapi.dto.request.ArticleCreateRequest;
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
import static com.leman.contentmanagementapi.constant.ArticleTestConstant.ARTICLE_RESPONSE;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    private JacksonTester<ArticleResponse> responseTester;

    @Test
    void create_ShouldReturn_Success() throws Exception {
        given(articleService.createArticle(ARTICLE_CREATE_REQUEST)).willReturn(ARTICLE_RESPONSE);

        mockMvc.perform(post(BASE_PATH)
                        .content(createTester.write(ARTICLE_CREATE_REQUEST).getJson())
                        .contentType("application/json"))
                .andExpect(status().isCreated())
                .andExpect(content().json(responseTester.write(ARTICLE_RESPONSE).getJson()));
    }

}
