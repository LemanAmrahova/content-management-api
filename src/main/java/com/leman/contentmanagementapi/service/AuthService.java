package com.leman.contentmanagementapi.service;

import com.leman.contentmanagementapi.dto.request.LoginRequest;
import com.leman.contentmanagementapi.dto.request.RegisterRequest;
import com.leman.contentmanagementapi.dto.response.LoginResponse;
import com.leman.contentmanagementapi.dto.response.UserResponse;
import com.leman.contentmanagementapi.entity.User;
import com.leman.contentmanagementapi.exception.DuplicateResourceException;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.leman.contentmanagementapi.constant.ApplicationConstant.TokenType;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserMapper userMapper;
    private final TokenMapper tokenMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public UserResponse registerUser(RegisterRequest request) {
        if(userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("User", "username", request.getUsername());
        }

        if(userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("User", "email", request.getEmail());
        }

        User entity = userMapper.toEntity(request);
        entity.setPassword(passwordEncoder.encode(request.getPassword()));
        User saved = userRepository.save(entity);
        log.info("User registered successfully with ID: {}", saved.getId());

        return userMapper.toResponse(saved);
    }

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),
                request.getPassword()));

        String accessToken = jwtService.generateAccessToken(request.getUsername());
        String refreshToken = jwtService.generateRefreshToken(request.getUsername());
        log.info("User logged in successfully: {}", request.getUsername());

        return tokenMapper.toLoginResponse(accessToken, refreshToken);
    }

    public LoginResponse refreshToken(String refreshToken) {
        if(!jwtService.validateToken(refreshToken)) {
            throw new UnauthorizedException(ErrorMessage.INVALID_REFRESH_TOKEN);
        }

        String tokenType = jwtService.getTokenType(refreshToken);
        if (!TokenType.REFRESH.equals(tokenType)) {
            throw new UnauthorizedException(ErrorMessage.TOKEN_MALFORMED);
        }

        String username = jwtService.getUsernameFromToken(refreshToken);
        String newAccessToken = jwtService.generateAccessToken(username);
        log.info("Access token refreshed for user: {}", username);

        return tokenMapper.toLoginResponse(newAccessToken, refreshToken);
    }

}
