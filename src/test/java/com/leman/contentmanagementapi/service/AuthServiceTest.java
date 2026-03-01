package com.leman.contentmanagementapi.service;

import com.leman.contentmanagementapi.constant.ApplicationConstant;
import com.leman.contentmanagementapi.enums.Role;
import com.leman.contentmanagementapi.exception.DuplicateResourceException;
import com.leman.contentmanagementapi.exception.ResourceNotFoundException;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;

import static com.leman.contentmanagementapi.constant.AuthTestConstant.ACCESS_TOKEN;
import static com.leman.contentmanagementapi.constant.AuthTestConstant.ENCODED_PASSWORD;
import static com.leman.contentmanagementapi.constant.AuthTestConstant.LOGIN_REQUEST;
import static com.leman.contentmanagementapi.constant.AuthTestConstant.LOGIN_RESPONSE;
import static com.leman.contentmanagementapi.constant.AuthTestConstant.REFRESH_TOKEN;
import static com.leman.contentmanagementapi.constant.AuthTestConstant.REGISTER_REQUEST;
import static com.leman.contentmanagementapi.constant.AuthTestConstant.USER_ENTITY;
import static com.leman.contentmanagementapi.constant.AuthTestConstant.USER_ID;
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

    @Mock
    Authentication authentication;

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
        given(authentication.getName()).willReturn(LOGIN_REQUEST.getUsername());
        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .willReturn(authentication);
        given(userRepository.findByUsername(LOGIN_REQUEST.getUsername())).willReturn(Optional.of(USER_ENTITY));
        given(jwtService.generateAccessToken(USER_ENTITY)).willReturn(ACCESS_TOKEN);
        given(jwtService.generateRefreshToken(USER_ENTITY)).willReturn(REFRESH_TOKEN);
        given(tokenMapper.toLoginResponse(ACCESS_TOKEN, REFRESH_TOKEN)).willReturn(LOGIN_RESPONSE);

        assertThat(authService.login(LOGIN_REQUEST)).isEqualTo(LOGIN_RESPONSE);

        then(authenticationManager).should().authenticate(any(UsernamePasswordAuthenticationToken.class));
        then(userRepository).should().findByUsername(LOGIN_REQUEST.getUsername());
        then(jwtService).should().generateAccessToken(USER_ENTITY);
        then(jwtService).should().generateRefreshToken(USER_ENTITY);
        then(tokenMapper).should().toLoginResponse(ACCESS_TOKEN, REFRESH_TOKEN);
    }

    @Test
    void login_ShouldThrow_WhenBadCredentials() {
        willThrow(new BadCredentialsException("bad")).given(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        assertThatThrownBy(() -> authService.login(LOGIN_REQUEST)).isInstanceOf(BadCredentialsException.class);

        then(authenticationManager).should().authenticate(any(UsernamePasswordAuthenticationToken.class));
        then(userRepository).shouldHaveNoInteractions();
        then(jwtService).shouldHaveNoInteractions();
        then(tokenMapper).shouldHaveNoInteractions();
    }

    @Test
    void login_ShouldThrow_WhenUserNotFound() {
        given(authentication.getName()).willReturn(LOGIN_REQUEST.getUsername());
        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .willReturn(authentication);
        given(userRepository.findByUsername(LOGIN_REQUEST.getUsername())).willReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(LOGIN_REQUEST)).isInstanceOf(ResourceNotFoundException.class);

        then(authenticationManager).should().authenticate(any(UsernamePasswordAuthenticationToken.class));
        then(userRepository).should().findByUsername(LOGIN_REQUEST.getUsername());
        then(jwtService).shouldHaveNoInteractions();
        then(tokenMapper).shouldHaveNoInteractions();
    }


    @Test
    void refreshToken_ShouldReturn_Success() {
        given(jwtService.getTokenType(REFRESH_TOKEN)).willReturn(ApplicationConstant.TokenType.REFRESH);
        given(jwtService.getUserIdFromToken(REFRESH_TOKEN)).willReturn(USER_ID);
        given(jwtService.validateToken(REFRESH_TOKEN, USER_ID)).willReturn(true);
        given(userRepository.findById(USER_ID)).willReturn(Optional.of(USER_ENTITY));
        given(jwtService.generateAccessToken(USER_ENTITY)).willReturn(ACCESS_TOKEN);
        given(tokenMapper.toLoginResponse(ACCESS_TOKEN, REFRESH_TOKEN)).willReturn(LOGIN_RESPONSE);

        assertThat(authService.refreshToken(REFRESH_TOKEN)).isEqualTo(LOGIN_RESPONSE);

        then(jwtService).should().getTokenType(REFRESH_TOKEN);
        then(jwtService).should().getUserIdFromToken(REFRESH_TOKEN);
        then(jwtService).should().validateToken(REFRESH_TOKEN, USER_ID);
        then(userRepository).should().findById(USER_ID);
        then(jwtService).should().generateAccessToken(USER_ENTITY);
        then(tokenMapper).should().toLoginResponse(ACCESS_TOKEN, REFRESH_TOKEN);
    }

    @Test
    void refreshToken_ShouldThrow_WhenTokenTypeNotRefresh() {
        given(jwtService.getTokenType(REFRESH_TOKEN)).willReturn(ApplicationConstant.TokenType.ACCESS);

        assertThatThrownBy(() -> authService.refreshToken(REFRESH_TOKEN)).isInstanceOf(UnauthorizedException.class);

        then(jwtService).should().getTokenType(REFRESH_TOKEN);
        then(jwtService).shouldHaveNoMoreInteractions();
        then(userRepository).shouldHaveNoInteractions();
        then(tokenMapper).shouldHaveNoInteractions();
    }

    @Test
    void refreshToken_ShouldThrow_WhenInvalidRefreshToken() {
        given(jwtService.getTokenType(REFRESH_TOKEN)).willReturn(ApplicationConstant.TokenType.REFRESH);
        given(jwtService.getUserIdFromToken(REFRESH_TOKEN)).willReturn(USER_ID);
        given(jwtService.validateToken(REFRESH_TOKEN, USER_ID)).willReturn(false);

        assertThatThrownBy(() -> authService.refreshToken(REFRESH_TOKEN)).isInstanceOf(UnauthorizedException.class);

        then(jwtService).should().getTokenType(REFRESH_TOKEN);
        then(jwtService).should().getUserIdFromToken(REFRESH_TOKEN);
        then(jwtService).should().validateToken(REFRESH_TOKEN, USER_ID);
        then(userRepository).shouldHaveNoInteractions();
        then(tokenMapper).shouldHaveNoInteractions();
    }

    @Test
    void refreshToken_ShouldThrow_WhenUserNotFound() {
        given(jwtService.getTokenType(REFRESH_TOKEN)).willReturn(ApplicationConstant.TokenType.REFRESH);
        given(jwtService.getUserIdFromToken(REFRESH_TOKEN)).willReturn(USER_ID);
        given(jwtService.validateToken(REFRESH_TOKEN, USER_ID)).willReturn(true);
        given(userRepository.findById(USER_ID)).willReturn(Optional.empty());

        assertThatThrownBy(() -> authService.refreshToken(REFRESH_TOKEN)).isInstanceOf(ResourceNotFoundException.class);

        then(jwtService).should().getTokenType(REFRESH_TOKEN);
        then(jwtService).should().getUserIdFromToken(REFRESH_TOKEN);
        then(jwtService).should().validateToken(REFRESH_TOKEN, USER_ID);
        then(userRepository).should().findById(USER_ID);
        then(tokenMapper).shouldHaveNoInteractions();
    }

}
