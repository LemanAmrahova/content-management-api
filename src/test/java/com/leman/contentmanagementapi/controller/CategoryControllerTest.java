package com.leman.contentmanagementapi.controller;

import com.leman.contentmanagementapi.dto.request.CategoryCreateRequest;
import com.leman.contentmanagementapi.dto.request.CategoryStatusChangeRequest;
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

import static com.leman.contentmanagementapi.constant.CategoryTestConstant.CATEGORY_CREATE_REQUEST;
import static com.leman.contentmanagementapi.constant.CategoryTestConstant.CATEGORY_RESPONSE;
import static com.leman.contentmanagementapi.constant.CategoryTestConstant.CATEGORY_STATUS_CHANGE_REQUEST;
import static com.leman.contentmanagementapi.constant.CategoryTestConstant.CATEGORY_UPDATE_REQUEST;
import static com.leman.contentmanagementapi.constant.TestConstant.ID;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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
    private JacksonTester<CategoryStatusChangeRequest> changeStatusTester;

    @Autowired
    private JacksonTester<CategoryResponse> responseTester;

    @Autowired
    private JacksonTester<List<CategoryResponse>> listResponseTester;

    @Test
    void create_ShouldReturn_Success() throws Exception {

        given(categoryService.createCategory(CATEGORY_CREATE_REQUEST)).willReturn(CATEGORY_RESPONSE);

        mockMvc.perform(post(BASE_PATH)
                        .content(createTester.write(CATEGORY_CREATE_REQUEST).getJson())
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

        given(categoryService.updateCategory(ID, CATEGORY_UPDATE_REQUEST)).willReturn(CATEGORY_RESPONSE);

        mockMvc.perform(put(BASE_PATH + "/" + ID)
                        .content(updateTester.write(CATEGORY_UPDATE_REQUEST).getJson())
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(responseTester.write(CATEGORY_RESPONSE).getJson()));
    }

    @Test
    void changeStatus_ShouldReturn_Success() throws Exception {

        willDoNothing().given(categoryService).changeCategoryStatus(ID, CATEGORY_STATUS_CHANGE_REQUEST);

        mockMvc.perform(patch(BASE_PATH + "/" + ID + "/status")
                        .content(changeStatusTester.write(CATEGORY_STATUS_CHANGE_REQUEST).getJson())
                        .contentType("application/json"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

}
