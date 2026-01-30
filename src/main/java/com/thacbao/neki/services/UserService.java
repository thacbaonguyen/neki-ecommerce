package com.thacbao.neki.services;

import com.thacbao.neki.dto.request.auth.ChangePasswordRequest;
import com.thacbao.neki.dto.request.auth.UserLoginRequest;
import com.thacbao.neki.dto.request.auth.UserRegisterRequest;
import com.thacbao.neki.dto.request.auth.UserUpdateRequest;
import com.thacbao.neki.dto.response.TokenResponse;
import com.thacbao.neki.dto.response.UserResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    void register(UserRegisterRequest request);

    TokenResponse login(UserLoginRequest request, String deviceInfo, String ipAddress);

    UserResponseDTO getProfile();

    void updateProfile(UserUpdateRequest request);

    void changePassword(ChangePasswordRequest request);

    Page<UserResponseDTO> getActiveUsers(Pageable pageable);

    Page<UserResponseDTO> getBlockedUsers(Pageable pageable);


    Page<UserResponseDTO> getUsersByRole(String roleName, Pageable pageable);

    UserResponseDTO getUserById(Integer userId);

    void toggleUserBlock(Integer userId, boolean block);

    void deleteUser(Integer userId);

    void verifyAccount(String email, String otp);

    void regenerateOtp(String email);

    void forgotPassword(String email);
    String verifyForgotPassword(String email, String otp);

    void setPassword(String token, String newPassword, String confirmPassword);

    TokenResponse refreshToken(String refreshToken, String deviceInfo, String ipAddress);
    void logout(String refreshToken);
    long countActiveVerifiedUsers();
}