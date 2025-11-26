package com.leman.contentmanagementapi.controller;

import com.leman.contentmanagementapi.dto.request.CategoryCreateRequest;
import com.leman.contentmanagementapi.dto.response.CategoryResponse;
import com.leman.contentmanagementapi.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.leman.contentmanagementapi.constant.CategoryTestConstant.CATEGORY_RESPONSE;
import static com.leman.contentmanagementapi.constant.CategoryTestConstant.CREATE_CATEGORY_REQUEST;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureJsonTesters
@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

    private static final String BASE_PATH = "/api/v1/categories";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

    @Autowired
    private JacksonTester<CategoryCreateRequest> createTester;

    @Autowired
    private JacksonTester<CategoryResponse> responseTester;

    @Test
    void create_ShouldReturn_Success() throws Exception {

        given(categoryService.createCategory(CREATE_CATEGORY_REQUEST))
                .willReturn(CATEGORY_RESPONSE);

        mockMvc.perform(post(BASE_PATH)
                        .content(createTester.write(CREATE_CATEGORY_REQUEST).getJson())
                        .contentType("application/json"))
                .andExpect(status().isCreated())
                .andExpect(content().json(responseTester.write(CATEGORY_RESPONSE).getJson()));
    }

}
