package com.leman.contentmanagementapi.controller;

import com.leman.contentmanagementapi.dto.request.PasswordChangeRequest;
import com.leman.contentmanagementapi.dto.request.UserUpdateRequest;
import com.leman.contentmanagementapi.dto.response.UserResponse;
import com.leman.contentmanagementapi.security.UserPrincipal;
import com.leman.contentmanagementapi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
        return ResponseEntity.ok(userService.getUserById(userPrincipal.getUser().getId()));
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateUser(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                   @RequestBody @Valid UserUpdateRequest request) {
        return ResponseEntity.ok(userService.updateUser(userPrincipal.getUser().getId(), request));
    }

    @PatchMapping("/me/password")
    public ResponseEntity<Void> changePassword(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                               @RequestBody @Valid PasswordChangeRequest request) {
        userService.changePassword(userPrincipal.getUser().getId(), request);
        return ResponseEntity.noContent().build();
    }

}
