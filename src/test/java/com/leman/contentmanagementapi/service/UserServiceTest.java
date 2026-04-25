package com.leman.contentmanagementapi.service;

import static com.leman.contentmanagementapi.constant.AdminTestConstant.USER_FILTER_REQUEST;
import static com.leman.contentmanagementapi.constant.UserTestConstant.ENCODED_PASSWORD;
import static com.leman.contentmanagementapi.constant.UserTestConstant.PASSWORD_CHANGE_REQUEST;
import static com.leman.contentmanagementapi.constant.UserTestConstant.SAME_PASSWORD_REQUEST;
import static com.leman.contentmanagementapi.constant.UserTestConstant.USER_ID;
import static com.leman.contentmanagementapi.constant.UserTestConstant.USER_RESPONSE;
import static com.leman.contentmanagementapi.constant.UserTestConstant.USER_UPDATE_REQUEST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import com.leman.contentmanagementapi.constant.UserTestConstant;
import com.leman.contentmanagementapi.dto.response.PageableResponse;
import com.leman.contentmanagementapi.dto.response.UserResponse;
import com.leman.contentmanagementapi.entity.User;
import com.leman.contentmanagementapi.exception.BadRequestException;
import com.leman.contentmanagementapi.exception.DuplicateResourceException;
import com.leman.contentmanagementapi.exception.ResourceNotFoundException;
import com.leman.contentmanagementapi.mapper.UserMapper;
import com.leman.contentmanagementapi.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Spy
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void findAllUsers_Should_Return_Success() {
        Page<User> userPage = mock(Page.class);
        given(userRepository.findAll(any(Specification.class), any(Pageable.class))).willReturn(userPage);

        PageableResponse<UserResponse> result = userService.findAllUsers(USER_FILTER_REQUEST);
        assertNotNull(result);

        then(userRepository).should(times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void getUserById_Should_Return_Success() {
        User userEntity = UserTestConstant.userEntity();
        given(userRepository.findById(USER_ID)).willReturn(Optional.of(userEntity));

        UserResponse result = userService.findUserById(USER_ID);
        assertNotNull(result);
        assertEquals(USER_RESPONSE, result);

        then(userRepository).should(times(1)).findById(USER_ID);
    }

    @Test
    void getUserById_Should_Throw_ResourceNotFoundException_WhenUserNotFound() {
        given(userRepository.findById(USER_ID)).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.findUserById(USER_ID));

        then(userRepository).should(times(1)).findById(USER_ID);
    }

    @Test
    void updateUser_Should_Return_Success() {
        User userEntity = UserTestConstant.userEntity();
        given(userRepository.findById(USER_ID)).willReturn(Optional.of(userEntity));
        given(userRepository.existsByUsernameAndIdNot(USER_UPDATE_REQUEST.getUsername(), USER_ID)).willReturn(false);
        given(userRepository.existsByEmailAndIdNot(USER_UPDATE_REQUEST.getEmail(), USER_ID)).willReturn(false);

        UserResponse result = userService.updateUser(USER_ID, USER_UPDATE_REQUEST);
        assertNotNull(result);
        assertEquals(USER_RESPONSE, result);

        then(userRepository).should(times(1)).findById(USER_ID);
        then(userRepository).should(times(1)).existsByUsernameAndIdNot(USER_UPDATE_REQUEST.getUsername(), USER_ID);
        then(userRepository).should(times(1)).existsByEmailAndIdNot(USER_UPDATE_REQUEST.getEmail(), USER_ID);
    }

    @Test
    void updateUser_Should_Throw_DuplicateResourceException_WhenUsernameExists() {
        User userEntity = UserTestConstant.userEntity();
        given(userRepository.findById(USER_ID)).willReturn(Optional.of(userEntity));
        given(userRepository.existsByUsernameAndIdNot(USER_UPDATE_REQUEST.getUsername(), USER_ID)).willReturn(true);

        assertThrows(DuplicateResourceException.class, () -> userService.updateUser(USER_ID, USER_UPDATE_REQUEST));

        then(userRepository).should(times(1)).findById(USER_ID);
        then(userRepository).should(times(1)).existsByUsernameAndIdNot(USER_UPDATE_REQUEST.getUsername(), USER_ID);
        then(userRepository).should(never()).existsByEmailAndIdNot(any(), any());
    }

    @Test
    void updateUser_Should_Throw_DuplicateResourceException_WhenEmailExists() {
        User userEntity = UserTestConstant.userEntity();
        given(userRepository.findById(USER_ID)).willReturn(Optional.of(userEntity));
        given(userRepository.existsByUsernameAndIdNot(USER_UPDATE_REQUEST.getUsername(), USER_ID)).willReturn(false);
        given(userRepository.existsByEmailAndIdNot(USER_UPDATE_REQUEST.getEmail(), USER_ID)).willReturn(true);

        assertThrows(DuplicateResourceException.class, () -> userService.updateUser(USER_ID, USER_UPDATE_REQUEST));

        then(userRepository).should(times(1)).findById(USER_ID);
        then(userRepository).should(times(1)).existsByUsernameAndIdNot(USER_UPDATE_REQUEST.getUsername(), USER_ID);
        then(userRepository).should(times(1)).existsByEmailAndIdNot(USER_UPDATE_REQUEST.getEmail(), USER_ID);
    }

    @Test
    void updateUser_Should_Throw_ResourceNotFoundException_WhenUserNotFound() {
        given(userRepository.findById(USER_ID)).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(USER_ID, USER_UPDATE_REQUEST));

        then(userRepository).should(times(1)).findById(USER_ID);
        then(userRepository).should(never()).existsByUsernameAndIdNot(any(), any());
    }

    @Test
    void changePassword_Should_Return_Success() {
        User userEntity = UserTestConstant.userEntity();
        given(userRepository.findById(USER_ID)).willReturn(Optional.of(userEntity));
        given(passwordEncoder.matches(eq(PASSWORD_CHANGE_REQUEST.getCurrentPassword()), any())).willReturn(true);
        given(passwordEncoder.encode(PASSWORD_CHANGE_REQUEST.getNewPassword())).willReturn(ENCODED_PASSWORD);

        userService.changePassword(USER_ID, PASSWORD_CHANGE_REQUEST);

        then(userRepository).should(times(1)).findById(USER_ID);
        then(passwordEncoder).should(times(1)).matches(eq(PASSWORD_CHANGE_REQUEST.getCurrentPassword()), any());
        then(passwordEncoder).should(times(1)).encode(PASSWORD_CHANGE_REQUEST.getNewPassword());
    }

    @Test
    void changePassword_Should_Throw_BadRequestException_WhenCurrentPasswordInvalid() {
        User userEntity = UserTestConstant.userEntity();
        given(userRepository.findById(USER_ID)).willReturn(Optional.of(userEntity));
        given(passwordEncoder.matches(eq(PASSWORD_CHANGE_REQUEST.getCurrentPassword()), any())).willReturn(false);

        assertThrows(BadRequestException.class, () -> userService.changePassword(USER_ID, PASSWORD_CHANGE_REQUEST));

        then(userRepository).should(times(1)).findById(USER_ID);
        then(passwordEncoder).should(times(1)).matches(eq(PASSWORD_CHANGE_REQUEST.getCurrentPassword()), any());
        then(passwordEncoder).should(never()).encode(any());
    }

    @Test
    void changePassword_Should_Throw_BadRequestException_WhenSamePassword() {
        User userEntity = UserTestConstant.userEntity();
        given(userRepository.findById(USER_ID)).willReturn(Optional.of(userEntity));
        given(passwordEncoder.matches(eq(SAME_PASSWORD_REQUEST.getCurrentPassword()), any())).willReturn(true);

        assertThrows(BadRequestException.class, () -> userService.changePassword(USER_ID, SAME_PASSWORD_REQUEST));

        then(userRepository).should(times(1)).findById(USER_ID);
        then(passwordEncoder).should(never()).encode(any());
    }

    @Test
    void changePassword_Should_Throw_ResourceNotFoundException_WhenUserNotFound() {
        given(userRepository.findById(USER_ID)).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.changePassword(USER_ID, PASSWORD_CHANGE_REQUEST));

        then(userRepository).should(times(1)).findById(USER_ID);
        then(passwordEncoder).shouldHaveNoInteractions();
    }

    @Test
    void deleteUser_Should_Return_Success() {
        User userEntity = UserTestConstant.userEntity();
        given(userRepository.findById(USER_ID)).willReturn(Optional.of(userEntity));

        userService.deleteUser(USER_ID);
        assertFalse(userEntity.isEnabled());

        then(userRepository).should(times(1)).findById(USER_ID);
        then(userRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void deleteUser_Should_Throw_ResourceNotFoundException_WhenUserNotFound() {
        given(userRepository.findById(USER_ID)).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(USER_ID));

        then(userRepository).should(times(1)).findById(USER_ID);
    }

}
