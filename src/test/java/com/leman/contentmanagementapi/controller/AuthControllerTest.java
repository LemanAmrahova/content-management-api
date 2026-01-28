package com.leman.contentmanagementapi.controller;

import com.leman.contentmanagementapi.annotation.ExcludeSecurityWebMvcTest;
import com.leman.contentmanagementapi.constant.ApplicationConstant;
import com.leman.contentmanagementapi.dto.request.LoginRequest;
import com.leman.contentmanagementapi.dto.request.RegisterRequest;
import com.leman.contentmanagementapi.dto.response.LoginResponse;
import com.leman.contentmanagementapi.dto.response.UserResponse;
import com.leman.contentmanagementapi.security.JwtService;
import com.leman.contentmanagementapi.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.leman.contentmanagementapi.constant.AuthTestConstant.LOGIN_REQUEST;
import static com.leman.contentmanagementapi.constant.AuthTestConstant.LOGIN_RESPONSE;
import static com.leman.contentmanagementapi.constant.AuthTestConstant.REFRESH_TOKEN;
import static com.leman.contentmanagementapi.constant.AuthTestConstant.REGISTER_REQUEST;
import static com.leman.contentmanagementapi.constant.AuthTestConstant.USER_RESPONSE;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExcludeSecurityWebMvcTest(controllers = AuthController.class)
@AutoConfigureJsonTesters
public class AuthControllerTest {

    private static final String BASE_PATH = "/api/v1/auth";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtService jwtService;

    @Autowired
    private JacksonTester<RegisterRequest> registerTester;

    @Autowired
    private JacksonTester<LoginRequest> loginTester;

    @Autowired
    private JacksonTester<UserResponse> userResponseTester;

    @Autowired
    private JacksonTester<LoginResponse> loginResponseTester;

    @Test
    void register_ShouldReturn_Success() throws Exception {
        given(authService.registerUser(REGISTER_REQUEST)).willReturn(USER_RESPONSE);

        mockMvc.perform(post(BASE_PATH + "/register")
                        .content(registerTester.write(REGISTER_REQUEST).getJson())
                        .contentType("application/json"))
                .andExpect(status().isCreated())
                .andExpect(content().json(userResponseTester.write(USER_RESPONSE).getJson()));
    }

    @Test
    void login_ShouldReturn_Success() throws Exception {
        given(authService.login(LOGIN_REQUEST)).willReturn(LOGIN_RESPONSE);

        mockMvc.perform(post(BASE_PATH + "/login")
                        .content(loginTester.write(LOGIN_REQUEST).getJson())
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(loginResponseTester.write(LOGIN_RESPONSE).getJson()));
    }

    @Test
    void refresh_ShouldReturn_Success() throws Exception {
        given(authService.refreshToken(REFRESH_TOKEN)).willReturn(LOGIN_RESPONSE);

        mockMvc.perform(post(BASE_PATH + "/refresh")
                        .header(ApplicationConstant.HttpHeader.REFRESH_TOKEN, REFRESH_TOKEN)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(loginResponseTester.write(LOGIN_RESPONSE).getJson()));
    }

}
