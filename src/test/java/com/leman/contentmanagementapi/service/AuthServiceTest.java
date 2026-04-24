package com.leman.contentmanagementapi.service;

import static com.leman.contentmanagementapi.constant.AuthTestConstant.ACCESS_TOKEN;
import static com.leman.contentmanagementapi.constant.AuthTestConstant.ENCODED_PASSWORD;
import static com.leman.contentmanagementapi.constant.AuthTestConstant.LOGIN_REQUEST;
import static com.leman.contentmanagementapi.constant.AuthTestConstant.LOGIN_RESPONSE;
import static com.leman.contentmanagementapi.constant.AuthTestConstant.REFRESH_TOKEN;
import static com.leman.contentmanagementapi.constant.AuthTestConstant.REGISTER_REQUEST;
import static com.leman.contentmanagementapi.constant.AuthTestConstant.USER_ENTITY;
import static com.leman.contentmanagementapi.constant.AuthTestConstant.USER_ID;
import static com.leman.contentmanagementapi.constant.AuthTestConstant.USER_RESPONSE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import com.leman.contentmanagementapi.constant.ApplicationConstant;
import com.leman.contentmanagementapi.dto.response.LoginResponse;
import com.leman.contentmanagementapi.dto.response.UserResponse;
import com.leman.contentmanagementapi.entity.User;
import com.leman.contentmanagementapi.enums.Role;
import com.leman.contentmanagementapi.exception.DuplicateResourceException;
import com.leman.contentmanagementapi.exception.ResourceNotFoundException;
import com.leman.contentmanagementapi.exception.UnauthorizedException;
import com.leman.contentmanagementapi.mapper.TokenMapper;
import com.leman.contentmanagementapi.mapper.UserMapper;
import com.leman.contentmanagementapi.repository.UserRepository;
import com.leman.contentmanagementapi.security.JwtService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Spy
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Spy
    private TokenMapper tokenMapper = Mappers.getMapper(TokenMapper.class);

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication authentication;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @InjectMocks
    private AuthService authService;

    @Test
    void registerUser_Should_Return_Success() {
        given(userRepository.existsByUsername(REGISTER_REQUEST.getUsername())).willReturn(false);
        given(userRepository.existsByEmail(REGISTER_REQUEST.getEmail())).willReturn(false);
        given(passwordEncoder.encode(REGISTER_REQUEST.getPassword())).willReturn(ENCODED_PASSWORD);
        given(userRepository.save(any(User.class))).willReturn(USER_ENTITY);

        UserResponse result = authService.registerUser(REGISTER_REQUEST);
        assertNotNull(result);
        assertEquals(USER_RESPONSE, result);

        then(userRepository).should(times(1)).existsByUsername(REGISTER_REQUEST.getUsername());
        then(userRepository).should(times(1)).existsByEmail(REGISTER_REQUEST.getEmail());
        then(userRepository).should(times(1)).save(userCaptor.capture());
        then(passwordEncoder).should(times(1)).encode(REGISTER_REQUEST.getPassword());

        assertEquals(ENCODED_PASSWORD, userCaptor.getValue().getPassword());
        assertEquals(Role.USER, userCaptor.getValue().getRole());
    }

    @Test
    void registerUser_Should_Throw_DuplicateResourceException_WhenUsernameExists() {
        given(userRepository.existsByUsername(REGISTER_REQUEST.getUsername())).willReturn(true);

        assertThrows(DuplicateResourceException.class, () -> authService.registerUser(REGISTER_REQUEST));

        then(userRepository).should(times(1)).existsByUsername(REGISTER_REQUEST.getUsername());
        then(userRepository).should(never()).save(any());
        then(passwordEncoder).shouldHaveNoInteractions();
    }

    @Test
    void registerUser_Should_Throw_DuplicateResourceException_WhenEmailExists() {
        given(userRepository.existsByUsername(REGISTER_REQUEST.getUsername())).willReturn(false);
        given(userRepository.existsByEmail(REGISTER_REQUEST.getEmail())).willReturn(true);

        assertThrows(DuplicateResourceException.class, () -> authService.registerUser(REGISTER_REQUEST));

        then(userRepository).should(times(1)).existsByUsername(REGISTER_REQUEST.getUsername());
        then(userRepository).should(times(1)).existsByEmail(REGISTER_REQUEST.getEmail());
        then(userRepository).should(never()).save(any());
        then(passwordEncoder).shouldHaveNoInteractions();
    }

    @Test
    void login_Should_Return_Success() {
        given(authentication.getName()).willReturn(LOGIN_REQUEST.getUsername());
        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .willReturn(authentication);
        given(userRepository.findByUsername(LOGIN_REQUEST.getUsername())).willReturn(Optional.of(USER_ENTITY));
        given(jwtService.generateAccessToken(USER_ENTITY)).willReturn(ACCESS_TOKEN);
        given(jwtService.generateRefreshToken(USER_ENTITY)).willReturn(REFRESH_TOKEN);

        LoginResponse result = authService.login(LOGIN_REQUEST);
        assertNotNull(result);
        assertEquals(LOGIN_RESPONSE, result);

        then(authenticationManager).should(times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        then(userRepository).should(times(1)).findByUsername(LOGIN_REQUEST.getUsername());
        then(jwtService).should(times(1)).generateAccessToken(USER_ENTITY);
        then(jwtService).should(times(1)).generateRefreshToken(USER_ENTITY);
    }

    @Test
    void login_Should_Throw_BadCredentialsException_WhenBadCredentials() {
        willThrow(new BadCredentialsException("bad")).given(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        assertThrows(BadCredentialsException.class, () -> authService.login(LOGIN_REQUEST));

        then(authenticationManager).should(times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        then(userRepository).shouldHaveNoInteractions();
        then(jwtService).shouldHaveNoInteractions();
    }

    @Test
    void login_Should_Throw_ResourceNotFoundException_WhenUserNotFound() {
        given(authentication.getName()).willReturn(LOGIN_REQUEST.getUsername());
        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .willReturn(authentication);
        given(userRepository.findByUsername(LOGIN_REQUEST.getUsername())).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> authService.login(LOGIN_REQUEST));

        then(authenticationManager).should(times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        then(userRepository).should(times(1)).findByUsername(LOGIN_REQUEST.getUsername());
        then(jwtService).shouldHaveNoInteractions();
    }

    @Test
    void refreshToken_Should_Return_Success() {
        given(jwtService.getTokenType(REFRESH_TOKEN)).willReturn(ApplicationConstant.TokenType.REFRESH);
        given(jwtService.getUserIdFromToken(REFRESH_TOKEN)).willReturn(USER_ID);
        given(jwtService.validateToken(REFRESH_TOKEN, USER_ID)).willReturn(true);
        given(userRepository.findById(USER_ID)).willReturn(Optional.of(USER_ENTITY));
        given(jwtService.generateAccessToken(USER_ENTITY)).willReturn(ACCESS_TOKEN);

        LoginResponse result = authService.refreshToken(REFRESH_TOKEN);
        assertNotNull(result);
        assertEquals(LOGIN_RESPONSE, result);

        then(jwtService).should(times(1)).getTokenType(REFRESH_TOKEN);
        then(jwtService).should(times(1)).getUserIdFromToken(REFRESH_TOKEN);
        then(jwtService).should(times(1)).validateToken(REFRESH_TOKEN, USER_ID);
        then(userRepository).should(times(1)).findById(USER_ID);
        then(jwtService).should(times(1)).generateAccessToken(USER_ENTITY);
    }

    @Test
    void refreshToken_Should_Throw_UnauthorizedException_WhenTokenTypeNotRefresh() {
        given(jwtService.getTokenType(REFRESH_TOKEN)).willReturn(ApplicationConstant.TokenType.ACCESS);

        assertThrows(UnauthorizedException.class, () -> authService.refreshToken(REFRESH_TOKEN));

        then(jwtService).should(times(1)).getTokenType(REFRESH_TOKEN);
        then(jwtService).shouldHaveNoMoreInteractions();
        then(userRepository).shouldHaveNoInteractions();
    }

    @Test
    void refreshToken_Should_Throw_UnauthorizedException_WhenInvalidRefreshToken() {
        given(jwtService.getTokenType(REFRESH_TOKEN)).willReturn(ApplicationConstant.TokenType.REFRESH);
        given(jwtService.getUserIdFromToken(REFRESH_TOKEN)).willReturn(USER_ID);
        given(jwtService.validateToken(REFRESH_TOKEN, USER_ID)).willReturn(false);

        assertThrows(UnauthorizedException.class, () -> authService.refreshToken(REFRESH_TOKEN));

        then(jwtService).should(times(1)).getTokenType(REFRESH_TOKEN);
        then(jwtService).should(times(1)).getUserIdFromToken(REFRESH_TOKEN);
        then(jwtService).should(times(1)).validateToken(REFRESH_TOKEN, USER_ID);
        then(userRepository).shouldHaveNoInteractions();
    }

    @Test
    void refreshToken_Should_Throw_ResourceNotFoundException_WhenUserNotFound() {
        given(jwtService.getTokenType(REFRESH_TOKEN)).willReturn(ApplicationConstant.TokenType.REFRESH);
        given(jwtService.getUserIdFromToken(REFRESH_TOKEN)).willReturn(USER_ID);
        given(jwtService.validateToken(REFRESH_TOKEN, USER_ID)).willReturn(true);
        given(userRepository.findById(USER_ID)).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> authService.refreshToken(REFRESH_TOKEN));

        then(jwtService).should(times(1)).getTokenType(REFRESH_TOKEN);
        then(jwtService).should(times(1)).getUserIdFromToken(REFRESH_TOKEN);
        then(jwtService).should(times(1)).validateToken(REFRESH_TOKEN, USER_ID);
        then(userRepository).should(times(1)).findById(USER_ID);
    }

}
