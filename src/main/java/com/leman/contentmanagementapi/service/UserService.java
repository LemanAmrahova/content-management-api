package com.leman.contentmanagementapi.service;

import static com.leman.contentmanagementapi.constant.ApplicationConstant.Common.EMAIL;
import static com.leman.contentmanagementapi.constant.ApplicationConstant.Common.ID;
import static com.leman.contentmanagementapi.constant.ApplicationConstant.Common.USERNAME;

import com.leman.contentmanagementapi.dto.request.UserUpdateRequest;
import com.leman.contentmanagementapi.dto.response.UserResponse;
import com.leman.contentmanagementapi.entity.User;
import com.leman.contentmanagementapi.exception.DuplicateResourceException;
import com.leman.contentmanagementapi.exception.ResourceNotFoundException;
import com.leman.contentmanagementapi.mapper.UserMapper;
import com.leman.contentmanagementapi.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private static final String ENTITY = "User";

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponse getUserById(Long userId) {
        User user = findExistingUser(userId);

        return userMapper.toResponse(user);
    }

    @Transactional
    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        User user = findExistingUser(id);

        validateUniqueUsername(request.getUsername(), id);
        validateUniqueEmail(request.getEmail(), id);

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        log.info("User updated successfully with ID: {}", id);

        return userMapper.toResponse(user);
    }

    private User findExistingUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(ENTITY, ID, userId));
    }

    private void validateUniqueUsername(String username, Long id) {
        if(userRepository.existsByUsernameAndIdNot(username, id)) {
            throw new DuplicateResourceException(ENTITY, USERNAME, username);
        }
    }

    private void validateUniqueEmail(String email, Long id) {
        if(userRepository.existsByEmailAndIdNot(email, id)) {
            throw new DuplicateResourceException(ENTITY, EMAIL, email);
        }
    }

}
