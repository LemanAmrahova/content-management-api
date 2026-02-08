package com.leman.contentmanagementapi.service;

import com.leman.contentmanagementapi.constant.ApplicationConstant;
import com.leman.contentmanagementapi.enums.Role;
import com.leman.contentmanagementapi.exception.DuplicateResourceException;
import com.leman.contentmanagementapi.exception.UnauthorizedException;
import com.leman.contentmanagementapi.mapper.TokenMapper;
import com.leman.contentmanagementapi.mapper.UserMapper;
import com.leman.contentmanagementapi.repository.UserRepository;
import com.leman.contentmanagementapi.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.leman.contentmanagementapi.constant.AuthTestConstant.ACCESS_TOKEN;
import static com.leman.contentmanagementapi.constant.AuthTestConstant.ENCODED_PASSWORD;
import static com.leman.contentmanagementapi.constant.AuthTestConstant.LOGIN_REQUEST;
import static com.leman.contentmanagementapi.constant.AuthTestConstant.LOGIN_RESPONSE;
import static com.leman.contentmanagementapi.constant.AuthTestConstant.REFRESH_TOKEN;
import static com.leman.contentmanagementapi.constant.AuthTestConstant.REGISTER_REQUEST;
import static com.leman.contentmanagementapi.constant.AuthTestConstant.USER_ENTITY;
import static com.leman.contentmanagementapi.constant.AuthTestConstant.USER_RESPONSE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private TokenMapper tokenMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    JwtService jwtService;

    @Mock
    AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @Test
    void registerUser_ShouldReturn_Success() throws Exception {
        given(userRepository.existsByUsername(REGISTER_REQUEST.getUsername())).willReturn(false);
        given(userRepository.existsByEmail(REGISTER_REQUEST.getEmail())).willReturn(false);
        given(userMapper.toEntity(REGISTER_REQUEST)).willReturn(USER_ENTITY);
        given(passwordEncoder.encode(REGISTER_REQUEST.getPassword())).willReturn(ENCODED_PASSWORD);
        given(userRepository.save(USER_ENTITY)).willReturn(USER_ENTITY);
        given(userMapper.toResponse(USER_ENTITY)).willReturn(USER_RESPONSE);

        assertThat(authService.registerUser(REGISTER_REQUEST)).isEqualTo(USER_RESPONSE);
        assertThat(USER_ENTITY.getPassword()).isEqualTo(ENCODED_PASSWORD);
        assertThat(USER_ENTITY.getRole()).isEqualTo(Role.USER);

        then(userRepository).should().existsByUsername(REGISTER_REQUEST.getUsername());
        then(userRepository).should().existsByEmail(REGISTER_REQUEST.getEmail());
        then(userMapper).should().toEntity(REGISTER_REQUEST);
        then(passwordEncoder).should().encode(REGISTER_REQUEST.getPassword());
        then(userRepository).should().save(USER_ENTITY);
        then(userMapper).should().toResponse(USER_ENTITY);
    }

    @Test
    void registerUser_ShouldThrow_WhenUsernameExists() {
        given(userRepository.existsByUsername(REGISTER_REQUEST.getUsername())).willReturn(true);

        assertThatThrownBy(() -> authService.registerUser(REGISTER_REQUEST))
                .isInstanceOf(DuplicateResourceException.class);

        then(userRepository).should().existsByUsername(REGISTER_REQUEST.getUsername());
        then(userRepository).shouldHaveNoMoreInteractions();
        then(userMapper).shouldHaveNoInteractions();
        then(passwordEncoder).shouldHaveNoInteractions();
    }

    @Test
    void registerUser_ShouldThrow_WhenEmailExists() {
        given(userRepository.existsByUsername(REGISTER_REQUEST.getUsername())).willReturn(false);
        given(userRepository.existsByEmail(REGISTER_REQUEST.getEmail())).willReturn(true);

        assertThatThrownBy(() -> authService.registerUser(REGISTER_REQUEST))
                .isInstanceOf(DuplicateResourceException.class);

        then(userRepository).should().existsByUsername(REGISTER_REQUEST.getUsername());
        then(userRepository).should().existsByEmail(REGISTER_REQUEST.getEmail());
        then(userRepository).shouldHaveNoMoreInteractions();
        then(userMapper).shouldHaveNoInteractions();
        then(passwordEncoder).shouldHaveNoInteractions();
    }

    @Test
    void login_ShouldReturn_Success() {
        given(jwtService.generateAccessToken(LOGIN_REQUEST.getUsername())).willReturn(ACCESS_TOKEN);
        given(jwtService.generateRefreshToken(LOGIN_REQUEST.getUsername())).willReturn(REFRESH_TOKEN);
        given(tokenMapper.toLoginResponse(ACCESS_TOKEN, REFRESH_TOKEN)).willReturn(LOGIN_RESPONSE);

        assertThat(authService.login(LOGIN_REQUEST)).isEqualTo(LOGIN_RESPONSE);

        then(authenticationManager).should().authenticate(any(UsernamePasswordAuthenticationToken.class));
        then(jwtService).should().generateAccessToken(LOGIN_REQUEST.getUsername());
        then(jwtService).should().generateRefreshToken(LOGIN_REQUEST.getUsername());
        then(tokenMapper).should().toLoginResponse(ACCESS_TOKEN, REFRESH_TOKEN);
    }

    @Test
    void login_ShouldThrow_WhenBadCredentials() {
        willThrow(new BadCredentialsException("bad")).given(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        assertThatThrownBy(() -> authService.login(LOGIN_REQUEST)).isInstanceOf(BadCredentialsException.class);

        then(authenticationManager).should().authenticate(any(UsernamePasswordAuthenticationToken.class));
        then(jwtService).shouldHaveNoInteractions();
        then(tokenMapper).shouldHaveNoInteractions();
    }

    @Test
    void refreshToken_ShouldReturn_Success() {
        given(jwtService.validateToken(REFRESH_TOKEN)).willReturn(true);
        given(jwtService.getTokenType(REFRESH_TOKEN)).willReturn(ApplicationConstant.TokenType.REFRESH);
        given(jwtService.getUsernameFromToken(REFRESH_TOKEN)).willReturn(LOGIN_REQUEST.getUsername());
        given(jwtService.generateAccessToken(LOGIN_REQUEST.getUsername())).willReturn(ACCESS_TOKEN);
        given(tokenMapper.toLoginResponse(ACCESS_TOKEN, REFRESH_TOKEN)).willReturn(LOGIN_RESPONSE);

        assertThat(authService.refreshToken(REFRESH_TOKEN)).isEqualTo(LOGIN_RESPONSE);

        then(jwtService).should().validateToken(REFRESH_TOKEN);
        then(jwtService).should().getTokenType(REFRESH_TOKEN);
        then(jwtService).should().getUsernameFromToken(REFRESH_TOKEN);
        then(jwtService).should().generateAccessToken(LOGIN_REQUEST.getUsername());
        then(tokenMapper).should().toLoginResponse(ACCESS_TOKEN, REFRESH_TOKEN);
    }

    @Test
    void refreshToken_ShouldThrow_WhenInvalidRefreshToken() {
        given(jwtService.validateToken(REFRESH_TOKEN)).willReturn(false);

        assertThatThrownBy(() -> authService.refreshToken(REFRESH_TOKEN)).isInstanceOf(UnauthorizedException.class);

        then(jwtService).should().validateToken(REFRESH_TOKEN);
        then(jwtService).shouldHaveNoMoreInteractions();
        then(tokenMapper).shouldHaveNoInteractions();
    }

    @Test
    void refreshToken_ShouldThrow_WhenTokenTypeNotRefresh() {
        given(jwtService.validateToken(REFRESH_TOKEN)).willReturn(true);
        given(jwtService.getTokenType(REFRESH_TOKEN)).willReturn(ApplicationConstant.TokenType.ACCESS);

        assertThatThrownBy(() -> authService.refreshToken(REFRESH_TOKEN)).isInstanceOf(UnauthorizedException.class);

        then(jwtService).should().validateToken(REFRESH_TOKEN);
        then(jwtService).should().getTokenType(REFRESH_TOKEN);
        then(jwtService).shouldHaveNoMoreInteractions();
        then(tokenMapper).shouldHaveNoInteractions();
    }

}
