package com.thacbao.neki.controllers;

import com.thacbao.neki.dto.request.ChangePasswordRequest;
import com.thacbao.neki.dto.request.UserLoginRequest;
import com.thacbao.neki.dto.request.UserRegisterRequest;
import com.thacbao.neki.dto.request.UserUpdateRequest;
import com.thacbao.neki.dto.response.ApiResponse;
import com.thacbao.neki.dto.response.UserResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody UserRegisterRequest userRegisterRequest) {
        return null;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@Valid @RequestBody UserLoginRequest userLoginRequest) {
        return null;
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getProfile() {
        return null;
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<Void>> updateProfile(@Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        return null;
    }

    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        return null;
    }

    @GetMapping("/users/active")
    public ResponseEntity<ApiResponse<Page<UserResponseDTO>>> getActiveUsers(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return null;
    }

    @GetMapping("/users/blocked")
    public ResponseEntity<ApiResponse<Page<UserResponseDTO>>> getBlockedUsers(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return null;
    }

    @GetMapping("/users/role/{roleName}")
    public ResponseEntity<ApiResponse<Page<UserResponseDTO>>> getUsersByRole(
            @PathVariable String roleName,
            @PageableDefault(size = 20) Pageable pageable) {
        return null;
    }
}
