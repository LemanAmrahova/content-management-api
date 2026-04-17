package com.leman.contentmanagementapi.service;

import com.leman.contentmanagementapi.exception.ResourceNotFoundException;
import com.leman.contentmanagementapi.mapper.UserMapper;
import com.leman.contentmanagementapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static com.leman.contentmanagementapi.constant.UserTestConstant.USER_ENTITY;
import static com.leman.contentmanagementapi.constant.UserTestConstant.USER_ID;
import static com.leman.contentmanagementapi.constant.UserTestConstant.USER_RESPONSE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void getUserById_ShouldReturn_Success() {
        given(userRepository.findById(USER_ID)).willReturn(Optional.of(USER_ENTITY));
        given(userMapper.toResponse(USER_ENTITY)).willReturn(USER_RESPONSE);

        assertThat(userService.getUserById(USER_ID)).isEqualTo(USER_RESPONSE);

        then(userRepository).should().findById(USER_ID);
        then(userMapper).should().toResponse(USER_ENTITY);
    }

    @Test
    void getUserById_ShouldThrow_WhenUserNotFound() {
        given(userRepository.findById(USER_ID)).willReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(USER_ID)).isInstanceOf(ResourceNotFoundException.class);

        then(userRepository).should().findById(USER_ID);
        then(userMapper).shouldHaveNoInteractions();
    }

}
