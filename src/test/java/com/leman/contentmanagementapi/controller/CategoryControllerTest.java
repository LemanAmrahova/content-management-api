package com.leman.contentmanagementapi.controller;

import com.leman.contentmanagementapi.dto.request.CategoryCreateRequest;
import com.leman.contentmanagementapi.dto.request.CategoryUpdateRequest;
import com.leman.contentmanagementapi.dto.response.CategoryResponse;
import com.leman.contentmanagementapi.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import static com.leman.contentmanagementapi.constant.CategoryTestConstant.CATEGORY_RESPONSE;
import static com.leman.contentmanagementapi.constant.CategoryTestConstant.CREATE_CATEGORY_REQUEST;
import static com.leman.contentmanagementapi.constant.CategoryTestConstant.UPDATE_CATEGORY_REQUEST;
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
    private JacksonTester<CategoryUpdateRequest> updateTester;

    @Autowired
    private JacksonTester<CategoryResponse> responseTester;

    @Autowired
    private JacksonTester<List<CategoryResponse>> listResponseTester;

    @Test
    void create_ShouldReturn_Success() throws Exception {

        given(categoryService.createCategory(CREATE_CATEGORY_REQUEST)).willReturn(CATEGORY_RESPONSE);

        mockMvc.perform(post(BASE_PATH)
                        .content(createTester.write(CREATE_CATEGORY_REQUEST).getJson())
                        .contentType("application/json"))
                .andExpect(status().isCreated())
                .andExpect(content().json(responseTester.write(CATEGORY_RESPONSE).getJson()));
    }

    @Test
    void getAll_ShouldReturn_Success() throws Exception {

        given(categoryService.findAllCategories()).willReturn(List.of(CATEGORY_RESPONSE));

        mockMvc.perform(get(BASE_PATH)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(listResponseTester.write(List.of(CATEGORY_RESPONSE)).getJson()));
    }

    @Test
    void getById_ShouldReturn_Success() throws Exception {

        given(categoryService.findCategoryById(ID)).willReturn(CATEGORY_RESPONSE);

        mockMvc.perform(get(BASE_PATH + "/" + ID)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(responseTester.write(CATEGORY_RESPONSE).getJson()));
    }

    @Test
    void update_ShouldReturn_Success() throws Exception {

        given(categoryService.updateCategory(ID, UPDATE_CATEGORY_REQUEST)).willReturn(CATEGORY_RESPONSE);

        mockMvc.perform(put(BASE_PATH + "/" + ID)
                        .content(updateTester.write(UPDATE_CATEGORY_REQUEST).getJson())
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(responseTester.write(CATEGORY_RESPONSE).getJson()));
    }

    @Test
    void delete_ShouldReturn_Success() throws Exception {

        willDoNothing().given(categoryService).deleteCategory(ID);

        mockMvc.perform(delete(BASE_PATH + "/" + ID)
                        .contentType("application/json"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

}
