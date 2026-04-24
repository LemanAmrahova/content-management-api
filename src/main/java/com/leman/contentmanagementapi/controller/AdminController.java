package com.leman.contentmanagementapi.controller;

import com.leman.contentmanagementapi.dto.request.UserFilterRequest;
import com.leman.contentmanagementapi.dto.response.PageableResponse;
import com.leman.contentmanagementapi.dto.response.UserResponse;
import com.leman.contentmanagementapi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/users")
public class AdminController {

    private final UserService userService;

    @PostMapping("/search")
    public ResponseEntity<PageableResponse<UserResponse>> getAll(@RequestBody @Valid UserFilterRequest request) {
        return ResponseEntity.ok(userService.findAllUsers(request));
    }

}
