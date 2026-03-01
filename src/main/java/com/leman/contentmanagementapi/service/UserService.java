package com.leman.contentmanagementapi.service;

import com.leman.contentmanagementapi.dto.response.UserResponse;
import com.leman.contentmanagementapi.entity.User;
import com.leman.contentmanagementapi.exception.ResourceNotFoundException;
import com.leman.contentmanagementapi.mapper.UserMapper;
import com.leman.contentmanagementapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponse getUserById(Long userId) {
        User user = findUserByIdOrThrow(userId);

        return userMapper.toResponse(user);
    }

    private User findUserByIdOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }

}
