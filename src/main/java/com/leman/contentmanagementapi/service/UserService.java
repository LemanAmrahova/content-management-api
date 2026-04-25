package com.leman.contentmanagementapi.service;

import static com.leman.contentmanagementapi.constant.ApplicationConstant.Common.EMAIL;
import static com.leman.contentmanagementapi.constant.ApplicationConstant.Common.ID;
import static com.leman.contentmanagementapi.constant.ApplicationConstant.Common.USERNAME;
import static com.leman.contentmanagementapi.exception.constant.ErrorMessage.INVALID_CURRENT_PASSWORD_ERROR_MESSAGE;
import static com.leman.contentmanagementapi.exception.constant.ErrorMessage.SAME_PASSWORD_ERROR_MESSAGE;

import com.leman.contentmanagementapi.dto.request.PasswordChangeRequest;
import com.leman.contentmanagementapi.dto.request.UserFilterRequest;
import com.leman.contentmanagementapi.dto.request.UserUpdateRequest;
import com.leman.contentmanagementapi.dto.response.PageableResponse;
import com.leman.contentmanagementapi.dto.response.UserResponse;
import com.leman.contentmanagementapi.entity.User;
import com.leman.contentmanagementapi.exception.BadRequestException;
import com.leman.contentmanagementapi.exception.DuplicateResourceException;
import com.leman.contentmanagementapi.exception.ResourceNotFoundException;
import com.leman.contentmanagementapi.mapper.UserMapper;
import com.leman.contentmanagementapi.repository.UserRepository;
import com.leman.contentmanagementapi.specification.UserSpecification;
import com.leman.contentmanagementapi.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    public PageableResponse<UserResponse> findAllUsers(UserFilterRequest request) {
        Pageable pageable = PaginationUtil.createPageable(request);
        Specification<User> userSpecification = UserSpecification.getSpecification(request);

        return userMapper.toPageableResponse(userRepository.findAll(userSpecification, pageable));
    }

    public UserResponse findUserById(Long userId) {
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

    @Transactional
    public void changePassword(Long id, PasswordChangeRequest request) {
        User user = findExistingUser(id);

        validateCurrentPassword(request.getCurrentPassword(), user.getPassword());
        validateNewPassword(request.getCurrentPassword(), request.getNewPassword());

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        log.info("User password changed successfully with ID: {}", id);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = findExistingUser(id);

        user.setEnabled(false);
        log.info("User deleted successfully with ID: {}", id);
    }

    private User findExistingUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> ResourceNotFoundException.of(ENTITY, ID, userId));
    }

    private void validateUniqueUsername(String username, Long id) {
        if (userRepository.existsByUsernameAndIdNot(username, id)) {
            throw DuplicateResourceException.of(ENTITY, USERNAME, username);
        }
    }

    private void validateUniqueEmail(String email, Long id) {
        if (userRepository.existsByEmailAndIdNot(email, id)) {
            throw DuplicateResourceException.of(ENTITY, EMAIL, email);
        }
    }

    private void validateCurrentPassword(String currentPassword, String encodedPassword) {
        if (!passwordEncoder.matches(currentPassword, encodedPassword)) {
            throw BadRequestException.of(INVALID_CURRENT_PASSWORD_ERROR_MESSAGE);
        }
    }

    private void validateNewPassword(String currentPassword, String newPassword) {
        if (currentPassword.equals(newPassword)) {
            throw BadRequestException.of(SAME_PASSWORD_ERROR_MESSAGE);
        }
    }

}
