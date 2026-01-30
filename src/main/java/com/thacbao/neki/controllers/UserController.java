package com.thacbao.neki.controllers;

import com.thacbao.neki.dto.request.auth.*;
import com.thacbao.neki.dto.response.ApiResponse;
import com.thacbao.neki.dto.response.TokenResponse;
import com.thacbao.neki.dto.response.UserResponseDTO;
import com.thacbao.neki.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> register(
            @Valid @RequestBody UserRegisterRequest request) {

        userService.register(request);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .code(HttpStatus.CREATED.value())
                .status("success")
                .message("Vui lòng kiểm tra email để xác thực tài khoản")
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(
            @Valid @RequestBody UserLoginRequest request,
            HttpServletRequest httpRequest) {

        String deviceInfo = httpRequest.getHeader("User-Agent");
        String ipAddress = getClientIp(httpRequest);

        TokenResponse tokens = userService.login(request, deviceInfo, ipAddress);

        ApiResponse<TokenResponse> response = ApiResponse.<TokenResponse>builder()
                .code(HttpStatus.OK.value())
                .status("success")
                .message("Đăng nhập thành công")
                .data(tokens)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-account")
    public ResponseEntity<ApiResponse<Void>> verifyAccount(
            @Valid @RequestBody VerifyAccountRequest request
            ) {

        userService.verifyAccount(request.getEmail(), request.getOtp());

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .status("success")
                .message("Xác thực email thành công")
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/regenerate-otp")
    public ResponseEntity<ApiResponse<Void>> regenerateOtp(
            @RequestParam @Email(message = "Email không hợp lệ") String email) {

        userService.regenerateOtp(email);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .status("success")
                .message("OTP mới đã được gửi đến email của bạn")
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {

        userService.forgotPassword(request.getEmail());

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .status("success")
                .message("Mã OTP đặt lại mật khẩu đã được gửi đến email của bạn")
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-forgot-password")
    public ResponseEntity<ApiResponse<Map<String, String>>> verifyForgotPassword(
            @Valid @RequestBody VerifyAccountRequest request) {

        String token = userService.verifyForgotPassword(request.getEmail(), request.getOtp());
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);

        ApiResponse<Map<String, String>> response = ApiResponse.<Map<String, String>>builder()
                .code(HttpStatus.OK.value())
                .status("success")
                .message("Xác thực OTP thành công")
                .data(tokenMap)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/set-password/{token}")
    public ResponseEntity<ApiResponse<Void>> setPassword(
            @PathVariable String token,
            @Valid @RequestBody SetPasswordRequest request) {

        userService.setPassword(token, request.getNewPassword(), request.getConfirmPassword());

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .status("success")
                .message("Đặt lại mật khẩu thành công")
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getProfile() {

        UserResponseDTO userDto = userService.getProfile();

        ApiResponse<UserResponseDTO> response = ApiResponse.<UserResponseDTO>builder()
                .code(HttpStatus.OK.value())
                .status("success")
                .message("Lấy thông tin người dùng thành công")
                .data(userDto)
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * Update current user
     */
    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<Void>> updateProfile(
            @Valid @RequestBody UserUpdateRequest request) {

        userService.updateProfile(request);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .status("success")
                .message("Cập nhật thông tin thành công")
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request) {

        userService.changePassword(request);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .status("success")
                .message("Đổi mật khẩu thành công")
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<TokenResponse>> refreshToken(
            @RequestParam @NotBlank(message = "Refresh token không được để trống") String refreshToken,
            HttpServletRequest httpRequest) {

        String deviceInfo = httpRequest.getHeader("User-Agent");
        String ipAddress = getClientIp(httpRequest);

        TokenResponse tokens = userService.refreshToken(refreshToken, deviceInfo, ipAddress);

        ApiResponse<TokenResponse> response = ApiResponse.<TokenResponse>builder()
                .code(HttpStatus.OK.value())
                .status("success")
                .message("Token đã được làm mới")
                .data(tokens)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @RequestParam @NotBlank(message = "Refresh token không được để trống") String refreshToken) {

        userService.logout(refreshToken);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .status("success")
                .message("Đăng xuất thành công")
                .build();

        return ResponseEntity.ok(response);
    }

    // ADMIN REQUEST

    @GetMapping("/users/active")
    public ResponseEntity<ApiResponse<Page<UserResponseDTO>>> getActiveUsers(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<UserResponseDTO> users = userService.getActiveUsers(pageable);

        ApiResponse<Page<UserResponseDTO>> response = ApiResponse.<Page<UserResponseDTO>>builder()
                .code(HttpStatus.OK.value())
                .status("success")
                .message("Lấy danh sách người dùng hoạt động thành công")
                .data(users)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/blocked")
    public ResponseEntity<ApiResponse<Page<UserResponseDTO>>> getBlockedUsers(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<UserResponseDTO> users = userService.getBlockedUsers(pageable);

        ApiResponse<Page<UserResponseDTO>> response = ApiResponse.<Page<UserResponseDTO>>builder()
                .code(HttpStatus.OK.value())
                .status("success")
                .message("Lấy danh sách người dùng bị khóa thành công")
                .data(users)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/role/{roleName}")
    public ResponseEntity<ApiResponse<Page<UserResponseDTO>>> getUsersByRole(
            @PathVariable String roleName,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<UserResponseDTO> users = userService.getUsersByRole(roleName, pageable);

        ApiResponse<Page<UserResponseDTO>> response = ApiResponse.<Page<UserResponseDTO>>builder()
                .code(HttpStatus.OK.value())
                .status("success")
                .message("Lấy danh sách người dùng theo vai trò thành công")
                .data(users)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUserById(
            @PathVariable Integer userId) {

        UserResponseDTO user = userService.getUserById(userId);

        ApiResponse<UserResponseDTO> response = ApiResponse.<UserResponseDTO>builder()
                .code(HttpStatus.OK.value())
                .status("success")
                .message("Lấy thông tin người dùng thành công")
                .data(user)
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/users/{userId}/block")
    public ResponseEntity<ApiResponse<Void>> blockUser(
            @PathVariable Integer userId) {

        userService.toggleUserBlock(userId, true);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .status("success")
                .message("Khóa người dùng thành công")
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/users/{userId}/unblock")
    public ResponseEntity<ApiResponse<Void>> unblockUser(
            @PathVariable Integer userId) {

        userService.toggleUserBlock(userId, false);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .status("success")
                .message("Mở khóa người dùng thành công")
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @PathVariable Integer userId) {

        userService.deleteUser(userId);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .status("success")
                .message("Xóa người dùng thành công")
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/stats/count")
    public ResponseEntity<ApiResponse<Map<String, Integer>>> getUserCount() {

        long count = userService.countActiveVerifiedUsers();
        Map<String, Integer> map = new HashMap<>();
        map.put("count", (int) count);

        ApiResponse<Map<String, Integer>> response = ApiResponse.<Map<String, Integer>>builder()
                .code(HttpStatus.OK.value())
                .status("success")
                .message("Lấy thống kê người dùng thành công")
                .data(map)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/test")
    public ResponseEntity<ApiResponse<String>> test() {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .code(HttpStatus.OK.value())
                .status("success")
                .message("API is working!")
                .data("User API v1.0 - With Refresh Token")
                .build();

        return ResponseEntity.ok(response);
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }
}