package com.leman.contentmanagementapi.controller;

import static com.leman.contentmanagementapi.constant.ArticleTestConstant.ARTICLE_CREATE_REQUEST;
import static com.leman.contentmanagementapi.constant.ArticleTestConstant.ARTICLE_DETAIL_RESPONSE;
import static com.leman.contentmanagementapi.constant.ArticleTestConstant.ARTICLE_FILTER_REQUEST;
import static com.leman.contentmanagementapi.constant.ArticleTestConstant.ARTICLE_RESPONSE;
import static com.leman.contentmanagementapi.constant.ArticleTestConstant.ARTICLE_UPDATE_REQUEST;
import static com.leman.contentmanagementapi.constant.ArticleTestConstant.PAGEABLE_ARTICLE_RESPONSE;
import static com.leman.contentmanagementapi.constant.TestConstant.ID;
import static com.leman.contentmanagementapi.constant.UserTestConstant.ADMIN_PRINCIPAL;
import static com.leman.contentmanagementapi.constant.UserTestConstant.USER_PRINCIPAL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.leman.contentmanagementapi.dto.request.ArticleCreateRequest;
import com.leman.contentmanagementapi.dto.request.ArticleFilterRequest;
import com.leman.contentmanagementapi.dto.request.ArticleUpdateRequest;
import com.leman.contentmanagementapi.dto.response.ArticleResponse;
import com.leman.contentmanagementapi.dto.response.PageableResponse;
import com.leman.contentmanagementapi.entity.User;
import com.leman.contentmanagementapi.security.JwtService;
import com.leman.contentmanagementapi.service.ArticleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ArticleController.class)
class ArticleControllerTest extends BaseControllerTest {

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
    private JacksonTester<ArticleResponse> detailResponseTester;

    @Autowired
    private JacksonTester<PageableResponse<ArticleResponse>> pageableDetailResponseTester;

    @Test
    void create_ShouldReturn_Success() throws Exception {
        given(articleService.createArticle(eq(ARTICLE_CREATE_REQUEST), any(User.class))).willReturn(ARTICLE_RESPONSE);

        mockMvc.perform(post(BASE_PATH)
                        .with(authentication(new UsernamePasswordAuthenticationToken(
                                USER_PRINCIPAL, null, USER_PRINCIPAL.getAuthorities())))
                        .content(createTester.write(ARTICLE_CREATE_REQUEST).getJson())
                        .contentType("application/json"))
                .andExpect(status().isCreated())
                .andExpect(content().json(responseTester.write(ARTICLE_RESPONSE).getJson()));

        then(articleService).should(times(1)).createArticle(eq(ARTICLE_CREATE_REQUEST), any(User.class));
    }

    @Test
    void getAll_ShouldReturn_Success() throws Exception {
        given(articleService.findAllArticles(any(ArticleFilterRequest.class))).willReturn(PAGEABLE_ARTICLE_RESPONSE);

        mockMvc.perform(post(BASE_PATH + "/search")
                        .with(authentication(new UsernamePasswordAuthenticationToken(
                                USER_PRINCIPAL, null, USER_PRINCIPAL.getAuthorities())))
                        .content(filterTester.write(ARTICLE_FILTER_REQUEST).getJson())
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(pageableDetailResponseTester.write(PAGEABLE_ARTICLE_RESPONSE).getJson()));

        then(articleService).should(times(1)).findAllArticles(any(ArticleFilterRequest.class));
    }

    @Test
    void getById_ShouldReturn_Success() throws Exception {
        given(articleService.findArticleById(ID)).willReturn(ARTICLE_DETAIL_RESPONSE);

        mockMvc.perform(get(BASE_PATH + "/" + ID)
                        .with(authentication(new UsernamePasswordAuthenticationToken(
                                USER_PRINCIPAL, null, USER_PRINCIPAL.getAuthorities())))
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(detailResponseTester.write(ARTICLE_DETAIL_RESPONSE).getJson()));

        then(articleService).should(times(1)).findArticleById(ID);
    }

    @Test
    void update_ShouldReturn_Success() throws Exception {
        given(articleService.updateArticle(eq(ID), eq(ARTICLE_UPDATE_REQUEST), any(Long.class))).willReturn(
                ARTICLE_RESPONSE);

        mockMvc.perform(put(BASE_PATH + "/" + ID)
                        .with(authentication(new UsernamePasswordAuthenticationToken(
                                USER_PRINCIPAL, null, USER_PRINCIPAL.getAuthorities())))
                        .content(updateTester.write(ARTICLE_UPDATE_REQUEST).getJson())
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(responseTester.write(ARTICLE_RESPONSE).getJson()));

        then(articleService).should(times(1)).updateArticle(eq(ID), eq(ARTICLE_UPDATE_REQUEST), any(Long.class));
    }

    @Test
    void publish_ShouldReturn_Success() throws Exception {
        willDoNothing().given(articleService).publishArticle(ID);

        mockMvc.perform(patch(BASE_PATH + "/" + ID + "/publish")
                        .with(authentication(new UsernamePasswordAuthenticationToken(
                                ADMIN_PRINCIPAL, null, ADMIN_PRINCIPAL.getAuthorities())))
                        .contentType("application/json"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        then(articleService).should(times(1)).publishArticle(ID);
    }

    @Test
    void delete_ShouldReturn_Success() throws Exception {
        willDoNothing().given(articleService).deleteArticle(eq(ID), any(User.class));

        mockMvc.perform(delete(BASE_PATH + "/" + ID)
                        .with(authentication(new UsernamePasswordAuthenticationToken(
                                USER_PRINCIPAL, null, USER_PRINCIPAL.getAuthorities())))
                        .contentType("application/json"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        then(articleService).should(times(1)).deleteArticle(eq(ID), any(User.class));
    }

}
