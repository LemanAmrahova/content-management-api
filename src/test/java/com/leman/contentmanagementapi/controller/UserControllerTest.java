package com.leman.contentmanagementapi.controller;

import static com.leman.contentmanagementapi.constant.UserTestConstant.USER_ID;
import static com.leman.contentmanagementapi.constant.UserTestConstant.USER_PRINCIPAL;
import static com.leman.contentmanagementapi.constant.UserTestConstant.USER_RESPONSE;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.leman.contentmanagementapi.dto.response.UserResponse;
import com.leman.contentmanagementapi.security.CustomUserDetailsService;
import com.leman.contentmanagementapi.security.JwtService;
import com.leman.contentmanagementapi.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
@AutoConfigureJsonTesters
class UserControllerTest {

    private static final String BASE_PATH = "/api/v1/users";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JacksonTester<UserResponse> responseTester;

    @Test
    void getCurrentUser_ShouldReturn_Success() throws Exception {
        given(userService.getUserById(USER_ID)).willReturn(USER_RESPONSE);

        mockMvc.perform(get(BASE_PATH + "/me")
                        .with(authentication(new UsernamePasswordAuthenticationToken(
                                USER_PRINCIPAL, null, USER_PRINCIPAL.getAuthorities()))))
                .andExpect(status().isOk())
                .andExpect(content().json(responseTester.write(USER_RESPONSE).getJson()));

        then(userService).should(times(1)).getUserById(USER_ID);
    }

}
