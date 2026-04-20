package com.leman.contentmanagementapi.controller;

import com.leman.contentmanagementapi.annotation.ExcludeSecurityWebMvcTest;
import com.leman.contentmanagementapi.dto.response.UserResponse;
import com.leman.contentmanagementapi.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.leman.contentmanagementapi.constant.UserTestConstant.USER_ID;
import static com.leman.contentmanagementapi.constant.UserTestConstant.USER_PRINCIPAL;
import static com.leman.contentmanagementapi.constant.UserTestConstant.USER_RESPONSE;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExcludeSecurityWebMvcTest(controllers = UserController.class)
@AutoConfigureJsonTesters
class UserControllerTest {

    private static final String BASE_PATH = "/api/v1/users";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private JacksonTester<UserResponse> responseTester;

    @Test
    void getCurrentUser_ShouldReturn_Success() throws Exception {
        given(userService.getUserById(USER_ID)).willReturn(USER_RESPONSE);

        Authentication auth = new UsernamePasswordAuthenticationToken(USER_PRINCIPAL, null,
                USER_PRINCIPAL.getAuthorities());

        mockMvc.perform(get(BASE_PATH + "/me")
                        .with(authentication(auth)))
                .andExpect(status().isOk())
                .andExpect(content().json(responseTester.write(USER_RESPONSE).getJson()));
    }

}
