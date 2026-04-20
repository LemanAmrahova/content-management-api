package com.leman.contentmanagementapi.service;

import static com.leman.contentmanagementapi.constant.ApplicationConstant.Common.EMAIL;
import static com.leman.contentmanagementapi.constant.ApplicationConstant.Common.ID;
import static com.leman.contentmanagementapi.constant.ApplicationConstant.Common.USERNAME;
import static com.leman.contentmanagementapi.constant.ApplicationConstant.TokenType;

import com.leman.contentmanagementapi.dto.request.LoginRequest;
import com.leman.contentmanagementapi.dto.request.RegisterRequest;
import com.leman.contentmanagementapi.dto.response.LoginResponse;
import com.leman.contentmanagementapi.dto.response.UserResponse;
import com.leman.contentmanagementapi.entity.User;
import com.leman.contentmanagementapi.enums.Role;
import com.leman.contentmanagementapi.exception.DuplicateResourceException;
import com.leman.contentmanagementapi.exception.ResourceNotFoundException;
import com.leman.contentmanagementapi.exception.UnauthorizedException;
import com.leman.contentmanagementapi.exception.constant.ErrorMessage;
import com.leman.contentmanagementapi.mapper.TokenMapper;
import com.leman.contentmanagementapi.mapper.UserMapper;
import com.leman.contentmanagementapi.repository.UserRepository;
import com.leman.contentmanagementapi.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private static final String ENTITY = "User";

    private final UserMapper userMapper;
    private final TokenMapper tokenMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public UserResponse registerUser(RegisterRequest request) {
        validateUniqueUsername(request.getUsername());
        validateUniqueEmail(request.getEmail());

        User user = createUserFromRequest(request);
        User savedUser = userRepository.save(user);
        log.info("User registered successfully with ID: {}", savedUser.getId());

        return userMapper.toResponse(savedUser);
    }

    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        User user = findUserByUsernameOrThrow(authentication);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        log.info("User logged in successfully with ID: {}", user.getId());

        return tokenMapper.toLoginResponse(accessToken, refreshToken);
    }

    public LoginResponse refreshToken(String refreshToken) {
        validateRefreshTokenType(refreshToken);

        Long userId = jwtService.getUserIdFromToken(refreshToken);
        validateRefreshToken(refreshToken, userId);

        User user = findUserByIdOrThrow(userId);
        String newAccessToken = jwtService.generateAccessToken(user);
        log.info("Access token refreshed for user ID: {}", userId);

        return tokenMapper.toLoginResponse(newAccessToken, refreshToken);
    }

    private User findUserByIdOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(ENTITY, ID, userId));
    }

    private User findUserByUsernameOrThrow(Authentication authentication) {
        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException(ENTITY, USERNAME, authentication.getName()));
    }

    private User createUserFromRequest(RegisterRequest request) {
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        return user;
    }

    private void validateUniqueUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new DuplicateResourceException(ENTITY, USERNAME, username);
        }
    }

    private void validateUniqueEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateResourceException(ENTITY, EMAIL, email);
        }
    }

    private void validateRefreshTokenType(String token) {
        String tokenType = jwtService.getTokenType(token);
        if (!TokenType.REFRESH.equals(tokenType)) {
            throw new UnauthorizedException(ErrorMessage.TOKEN_MALFORMED);
        }
    }

    private void validateRefreshToken(String token, Long userId) {
        if (!jwtService.validateToken(token, userId)) {
            throw new UnauthorizedException(ErrorMessage.INVALID_REFRESH_TOKEN);
        }
    }

}
