package com.leman.contentmanagementapi.controller;

import static com.leman.contentmanagementapi.constant.UserTestConstant.PASSWORD_CHANGE_REQUEST;
import static com.leman.contentmanagementapi.constant.UserTestConstant.USER_ID;
import static com.leman.contentmanagementapi.constant.UserTestConstant.USER_PRINCIPAL;
import static com.leman.contentmanagementapi.constant.UserTestConstant.USER_RESPONSE;
import static com.leman.contentmanagementapi.constant.UserTestConstant.USER_UPDATE_REQUEST;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.leman.contentmanagementapi.config.SecurityConfig;
import com.leman.contentmanagementapi.dto.request.PasswordChangeRequest;
import com.leman.contentmanagementapi.dto.request.UserUpdateRequest;
import com.leman.contentmanagementapi.dto.response.UserResponse;
import com.leman.contentmanagementapi.security.CustomUserDetailsService;
import com.leman.contentmanagementapi.security.JwtService;
import com.leman.contentmanagementapi.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
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
    private JacksonTester<PasswordChangeRequest> passwordChangeTester;

    @Autowired
    private JacksonTester<UserUpdateRequest> updateTester;

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

    @Test
    void updateUser_ShouldReturn_Success() throws Exception {
        given(userService.updateUser(USER_ID, USER_UPDATE_REQUEST)).willReturn(USER_RESPONSE);

        mockMvc.perform(put(BASE_PATH + "/me")
                        .with(authentication(new UsernamePasswordAuthenticationToken(
                                USER_PRINCIPAL, null, USER_PRINCIPAL.getAuthorities())))
                        .content(updateTester.write(USER_UPDATE_REQUEST).getJson())
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(responseTester.write(USER_RESPONSE).getJson()));

        then(userService).should(times(1)).updateUser(USER_ID, USER_UPDATE_REQUEST);
    }

    @Test
    void changePassword_ShouldReturn_Success() throws Exception {
        willDoNothing().given(userService).changePassword(USER_ID, PASSWORD_CHANGE_REQUEST);

        mockMvc.perform(patch(BASE_PATH + "/me/password")
                        .with(authentication(new UsernamePasswordAuthenticationToken(
                                USER_PRINCIPAL, null, USER_PRINCIPAL.getAuthorities())))
                        .content(passwordChangeTester.write(PASSWORD_CHANGE_REQUEST).getJson())
                        .contentType("application/json"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        then(userService).should(times(1)).changePassword(USER_ID, PASSWORD_CHANGE_REQUEST);
    }

}
