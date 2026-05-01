package com.leman.contentmanagementapi.controller;

import static com.leman.contentmanagementapi.constant.CategoryTestConstant.CATEGORY_CREATE_REQUEST;
import static com.leman.contentmanagementapi.constant.CategoryTestConstant.CATEGORY_RESPONSE;
import static com.leman.contentmanagementapi.constant.CategoryTestConstant.CATEGORY_STATUS_CHANGE_REQUEST;
import static com.leman.contentmanagementapi.constant.CategoryTestConstant.CATEGORY_UPDATE_REQUEST;
import static com.leman.contentmanagementapi.constant.TestConstant.ID;
import static com.leman.contentmanagementapi.constant.UserTestConstant.ADMIN_PRINCIPAL;
import static com.leman.contentmanagementapi.constant.UserTestConstant.USER_PRINCIPAL;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.leman.contentmanagementapi.dto.request.CategoryCreateRequest;
import com.leman.contentmanagementapi.dto.request.CategoryStatusChangeRequest;
import com.leman.contentmanagementapi.dto.request.CategoryUpdateRequest;
import com.leman.contentmanagementapi.dto.response.CategoryResponse;
import com.leman.contentmanagementapi.service.CategoryService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest extends BaseControllerTest {

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
                        .with(authentication(new UsernamePasswordAuthenticationToken(
                                ADMIN_PRINCIPAL, null, ADMIN_PRINCIPAL.getAuthorities())))
                        .content(createTester.write(CATEGORY_CREATE_REQUEST).getJson())
                        .contentType("application/json"))
                .andExpect(status().isCreated())
                .andExpect(content().json(responseTester.write(CATEGORY_RESPONSE).getJson()));

        then(categoryService).should(times(1)).createCategory(CATEGORY_CREATE_REQUEST);
    }

    @Test
    void getAll_ShouldReturn_Success() throws Exception {
        given(categoryService.findAllCategories()).willReturn(List.of(CATEGORY_RESPONSE));

        mockMvc.perform(get(BASE_PATH)
                        .with(authentication(new UsernamePasswordAuthenticationToken(
                                USER_PRINCIPAL, null, USER_PRINCIPAL.getAuthorities())))
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(listResponseTester.write(List.of(CATEGORY_RESPONSE)).getJson()));

        then(categoryService).should(times(1)).findAllCategories();
    }

    @Test
    void getById_ShouldReturn_Success() throws Exception {
        given(categoryService.findCategoryById(ID)).willReturn(CATEGORY_RESPONSE);

        mockMvc.perform(get(BASE_PATH + "/" + ID)
                        .with(authentication(new UsernamePasswordAuthenticationToken(
                                USER_PRINCIPAL, null, USER_PRINCIPAL.getAuthorities())))
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(responseTester.write(CATEGORY_RESPONSE).getJson()));

        then(categoryService).should(times(1)).findCategoryById(ID);
    }

    @Test
    void update_ShouldReturn_Success() throws Exception {
        given(categoryService.updateCategory(ID, CATEGORY_UPDATE_REQUEST)).willReturn(CATEGORY_RESPONSE);

        mockMvc.perform(put(BASE_PATH + "/" + ID)
                        .with(authentication(new UsernamePasswordAuthenticationToken(
                                ADMIN_PRINCIPAL, null, ADMIN_PRINCIPAL.getAuthorities())))
                        .content(updateTester.write(CATEGORY_UPDATE_REQUEST).getJson())
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(responseTester.write(CATEGORY_RESPONSE).getJson()));

        then(categoryService).should(times(1)).updateCategory(ID, CATEGORY_UPDATE_REQUEST);
    }

    @Test
    void updateStatus_ShouldReturn_Success() throws Exception {
        willDoNothing().given(categoryService).updateCategoryStatus(ID, CATEGORY_STATUS_CHANGE_REQUEST);

        mockMvc.perform(patch(BASE_PATH + "/" + ID + "/status")
                        .with(authentication(new UsernamePasswordAuthenticationToken(
                                ADMIN_PRINCIPAL, null, ADMIN_PRINCIPAL.getAuthorities())))
                        .content(changeStatusTester.write(CATEGORY_STATUS_CHANGE_REQUEST).getJson())
                        .contentType("application/json"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        then(categoryService).should(times(1)).updateCategoryStatus(ID, CATEGORY_STATUS_CHANGE_REQUEST);
    }

}
