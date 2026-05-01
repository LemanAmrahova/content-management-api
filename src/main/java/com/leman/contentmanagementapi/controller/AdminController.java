package com.leman.contentmanagementapi.controller;

import com.leman.contentmanagementapi.dto.request.UserFilterRequest;
import com.leman.contentmanagementapi.dto.response.PageableResponse;
import com.leman.contentmanagementapi.dto.response.UserResponse;
import com.leman.contentmanagementapi.enums.Role;
import com.leman.contentmanagementapi.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/v1/admin/users")
public class AdminController {

    private final UserService userService;

    @PostMapping("/search")
    public ResponseEntity<PageableResponse<UserResponse>> getAllUsers(@RequestBody @Valid UserFilterRequest request) {
        return ResponseEntity.ok(userService.findAllUsers(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(userService.findUserById(id));
    }

    @PatchMapping("/{id}/role")
    public ResponseEntity<Void> updateUserRole(@PathVariable @Positive Long id, @RequestParam Role role) {
        userService.updateRole(id, role);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateUserStatus(@PathVariable @Positive Long id,
                                                 @RequestParam Boolean enabled) {
        userService.updateUserStatus(id, enabled);
        return ResponseEntity.noContent().build();
    }

}
