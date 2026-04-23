package com.leman.contentmanagementapi.service;

import static com.leman.contentmanagementapi.constant.UserTestConstant.USER_ENTITY;
import static com.leman.contentmanagementapi.constant.UserTestConstant.USER_ID;
import static com.leman.contentmanagementapi.constant.UserTestConstant.USER_RESPONSE;
import static com.leman.contentmanagementapi.constant.UserTestConstant.USER_UPDATE_REQUEST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import com.leman.contentmanagementapi.dto.response.UserResponse;
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

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Spy
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void getUserById_Should_Return_Success() {
        given(userRepository.findById(USER_ID)).willReturn(Optional.of(USER_ENTITY));

        UserResponse result = userService.getUserById(USER_ID);
        assertNotNull(result);
        assertEquals(USER_RESPONSE, result);

        then(userRepository).should(times(1)).findById(USER_ID);
    }

    @Test
    void getUserById_Should_Throw_ResourceNotFoundException_WhenUserNotFound() {
        given(userRepository.findById(USER_ID)).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(USER_ID));

        then(userRepository).should(times(1)).findById(USER_ID);
    }

    @Test
    void updateUser_Should_Return_Success() {
        given(userRepository.findById(USER_ID)).willReturn(Optional.of(USER_ENTITY));
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
        given(userRepository.findById(USER_ID)).willReturn(Optional.of(USER_ENTITY));
        given(userRepository.existsByUsernameAndIdNot(USER_UPDATE_REQUEST.getUsername(), USER_ID)).willReturn(true);

        assertThrows(DuplicateResourceException.class, () -> userService.updateUser(USER_ID, USER_UPDATE_REQUEST));

        then(userRepository).should(times(1)).findById(USER_ID);
        then(userRepository).should(times(1)).existsByUsernameAndIdNot(USER_UPDATE_REQUEST.getUsername(), USER_ID);
        then(userRepository).should(never()).existsByEmailAndIdNot(any(), any());
    }

    @Test
    void updateUser_Should_Throw_DuplicateResourceException_WhenEmailExists() {
        given(userRepository.findById(USER_ID)).willReturn(Optional.of(USER_ENTITY));
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

}
