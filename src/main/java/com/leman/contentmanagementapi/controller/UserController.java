package com.leman.contentmanagementapi.controller;

import com.leman.contentmanagementapi.dto.response.UserResponse;
import com.leman.contentmanagementapi.security.UserPrincipal;
import com.leman.contentmanagementapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = userPrincipal.getUser().getId();
        return ResponseEntity.ok(userService.getUserById(userId));
    }

}
