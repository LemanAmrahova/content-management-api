package com.leman.contentmanagementapi.controller;

import static com.leman.contentmanagementapi.constant.AdminTestConstant.PAGEABLE_USER_RESPONSE;
import static com.leman.contentmanagementapi.constant.AdminTestConstant.USER_FILTER_REQUEST;
import static com.leman.contentmanagementapi.constant.TestConstant.ID;
import static com.leman.contentmanagementapi.constant.UserTestConstant.USER_RESPONSE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.leman.contentmanagementapi.annotation.ExcludeSecurityWebMvcTest;
import com.leman.contentmanagementapi.dto.request.UserFilterRequest;
import com.leman.contentmanagementapi.dto.response.PageableResponse;
import com.leman.contentmanagementapi.dto.response.UserResponse;
import com.leman.contentmanagementapi.security.JwtService;
import com.leman.contentmanagementapi.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@ExcludeSecurityWebMvcTest(controllers = AdminController.class)
@AutoConfigureJsonTesters
class AdminControllerTest {

    private static final String BASE_PATH = "/api/v1/admin/users";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtService jwtService;

    @Autowired
    private JacksonTester<UserFilterRequest> filterTester;

    @Autowired
    private JacksonTester<UserResponse> responseTester;

    @Autowired
    private JacksonTester<PageableResponse<UserResponse>> pageableResponseTester;

    @Test
    void getAll_ShouldReturn_Success() throws Exception {
        given(userService.findAllUsers(any(UserFilterRequest.class))).willReturn(PAGEABLE_USER_RESPONSE);

        mockMvc.perform(post(BASE_PATH + "/search")
                        .content(filterTester.write(USER_FILTER_REQUEST).getJson())
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(pageableResponseTester.write(PAGEABLE_USER_RESPONSE).getJson()));

        then(userService).should(times(1)).findAllUsers(any(UserFilterRequest.class));
    }

    @Test
    void getUserById_ShouldReturn_Success() throws Exception {
        given(userService.findUserById(ID)).willReturn(USER_RESPONSE);

        mockMvc.perform(get(BASE_PATH + "/" + ID)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(responseTester.write(USER_RESPONSE).getJson()));

        then(userService).should(times(1)).findUserById(ID);
    }

}
