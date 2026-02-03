package com.thacbao.neki.services.impl;

import com.thacbao.neki.configurations.JwtUtils;
import com.thacbao.neki.dto.request.auth.ChangePasswordRequest;
import com.thacbao.neki.dto.request.auth.UserLoginRequest;
import com.thacbao.neki.dto.request.auth.UserRegisterRequest;
import com.thacbao.neki.dto.request.auth.UserUpdateRequest;
import com.thacbao.neki.dto.response.TokenResponse;
import com.thacbao.neki.dto.response.UserResponseDTO;
import com.thacbao.neki.exceptions.common.AlreadyException;
import com.thacbao.neki.exceptions.common.InvalidException;
import com.thacbao.neki.exceptions.common.NotFoundException;
import com.thacbao.neki.exceptions.user.OtpExpiredException;
import com.thacbao.neki.exceptions.user.PermissionException;
import com.thacbao.neki.model.RefreshToken;
import com.thacbao.neki.model.Role;
import com.thacbao.neki.model.User;
import com.thacbao.neki.repositories.jpa.RoleRepository;
import com.thacbao.neki.repositories.jpa.UserRepository;
import com.thacbao.neki.security.SecurityUtils;
import com.thacbao.neki.services.EmailService;
import com.thacbao.neki.services.TokenService;
import com.thacbao.neki.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final TokenService tokenService;

    @Value("${jwt.accessToken.expirationHours:24}")
    private int accessTokenExpirationHours;

    private static final int OTP_LENGTH = 6;
    private static final SecureRandom random = new SecureRandom();

    @Override
    public void register(UserRegisterRequest request) {
        log.info("Registering new user with email: {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AlreadyException("Email đã được sử dụng");
        }

        // Validate phone
        if (request.getPhone() != null && !request.getPhone().isEmpty()) {
            if (userRepository.existsByPhone(request.getPhone())) {
                throw new AlreadyException("Số điện thoại đã được sử dụng");
            }
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new InvalidException("Mật khẩu xác nhận không khớp");
        }

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new NotFoundException("Role USER không tồn tại"));

        String otp = generateOTP();

        User user = User.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .otp(otp)
                .otpGenerateTime(LocalDateTime.now())
                .isActive(true)
                .emailVerified(false)
                .provider(request.getProvider())
                .roles(new HashSet<>(Collections.singletonList(userRole)))
                .build();

        userRepository.save(user);

        emailService.sendOtpEmail(user.getEmail(), otp);
        log.info("User registered successfully: {}", user.getEmail());
    }

    @Override
    public TokenResponse login(UserLoginRequest request, String deviceInfo, String ipAddress) {
        log.info("User login attempt: {}", request.getEmail());

        try {
            // Authenticate
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new NotFoundException("Người dùng không tồn tại"));

            if (!user.getIsActive()) {
                throw new InvalidException("Tài khoản đã bị khóa");
            }

            if (!user.getEmailVerified()) {
                throw new InvalidException("Vui lòng xác thực email trước khi đăng nhập");
            }

            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", user.getId());
            claims.put("roles", user.getRoles().stream()
                    .map(Role::getName)
                    .toList());

            String accessToken = jwtUtils.generateToken(user.getEmail(), claims);

            // Create refresh token
            RefreshToken refreshToken = tokenService.createRefreshToken(user, deviceInfo, ipAddress);

            log.info("User logged in successfully: {}", user.getEmail());

            return TokenResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken.getToken())
                    .tokenType("Bearer")
                    .expiresIn((long) accessTokenExpirationHours * 3600)
                    .user(UserResponseDTO.from(user))
                    .build();

        } catch (AuthenticationException e) {
            log.error("Authentication failed for user: {}", request.getEmail());
            throw new InvalidException("Email hoặc mật khẩu không chính xác");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getProfile() {
        Integer userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            throw new InvalidException("Người dùng chưa đăng nhập");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Người dùng không tồn tại"));

        return UserResponseDTO.from(user);
    }

    @Override
    public void updateProfile(UserUpdateRequest request) {
        Integer userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            throw new InvalidException("Người dùng chưa đăng nhập");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Người dùng không tồn tại"));

        if (request.getFullName() != null && !request.getFullName().trim().isEmpty()) {
            user.setFullName(request.getFullName().trim());
        }

        if (request.getPhone() != null && !request.getPhone().trim().isEmpty()) {
            userRepository.findByEmail(request.getPhone())
                    .ifPresent(existingUser -> {
                        if (!existingUser.getId().equals(userId)) {
                            throw new AlreadyException("Số điện thoại đã được sử dụng");
                        }
                    });
            user.setPhone(request.getPhone().trim());
        }

        userRepository.save(user);
        log.info("Profile updated for user: {}", user.getEmail());
    }

    @Override
    public void changePassword(ChangePasswordRequest request) {
        Integer userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            throw new InvalidException("Người dùng chưa đăng nhập");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Người dùng không tồn tại"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPasswordHash())) {
            throw new InvalidException("Mật khẩu cũ không chính xác");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new InvalidException("Mật khẩu xác nhận không khớp");
        }

        if (passwordEncoder.matches(request.getNewPassword(), user.getPasswordHash())) {
            throw new InvalidException("Mật khẩu mới không được trùng với mật khẩu cũ");
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        tokenService.revokeAllUserTokens(userId);

        emailService.sendPasswordChangedEmail(user.getEmail(), user.getFullName());

        log.info("Password changed for user: {}", user.getEmail());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDTO> getActiveUsers(Pageable pageable) {
        if (!SecurityUtils.hasRole("ADMIN")) {
            throw new PermissionException("Bạn không có quyền truy cập");
        }

        Page<User> users = userRepository.findAllActiveUsers(pageable);
        return users.map(UserResponseDTO::from);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDTO> getBlockedUsers(Pageable pageable) {
        if (!SecurityUtils.hasRole("ADMIN")) {
            throw new PermissionException("Bạn không có quyền truy cập");
        }

        Page<User> users = userRepository.findAllBlockedUsers(pageable);
        return users.map(UserResponseDTO::from);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDTO> getUsersByRole(String roleName, Pageable pageable) {
        if (!SecurityUtils.hasRole("ADMIN")) {
            throw new PermissionException("Bạn không có quyền truy cập");
        }

        roleRepository.findByName(roleName.toUpperCase())
                .orElseThrow(() -> new NotFoundException("Role không tồn tại: " + roleName));

        Page<User> users = userRepository.findUsersByRole(roleName.toUpperCase(), pageable);
        return users.map(UserResponseDTO::from);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Integer userId) {
        Integer currentUserId = SecurityUtils.getCurrentUserId();
        if (!SecurityUtils.hasRole("ADMIN") && !userId.equals(currentUserId)) {
            throw new PermissionException("Bạn không có quyền truy cập");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Người dùng không tồn tại"));

        return UserResponseDTO.from(user);
    }

    @Override
    public void toggleUserBlock(Integer userId, boolean block) {
        if (!SecurityUtils.hasRole("ADMIN")) {
            throw new PermissionException("Bạn không có quyền thực hiện thao tác này");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Người dùng không tồn tại"));
        Integer currentUserId = SecurityUtils.getCurrentUserId();
        if (userId.equals(currentUserId)) {
            throw new InvalidException("Bạn không thể khóa chính mình");
        }

        user.setIsActive(!block);
        userRepository.save(user);
        if (block) {
            tokenService.revokeAllUserTokens(userId);
            emailService.sendAccountBlockedEmail(user.getEmail(), user.getFullName());
        }

        log.info("User {} has been {}", user.getEmail(), block ? "blocked" : "unblocked");
    }

    @Override
    public void deleteUser(Integer userId) {
        if (!SecurityUtils.hasRole("ADMIN")) {
            throw new PermissionException("Bạn không có quyền thực hiện thao tác này");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Người dùng không tồn tại"));
        Integer currentUserId = SecurityUtils.getCurrentUserId();
        if (userId.equals(currentUserId)) {
            throw new InvalidException("Bạn không thể xóa chính mình");
        }

        userRepository.delete(user);
        log.info("User deleted: {}", user.getEmail());
    }

    @Override
    public void verifyAccount(String email, String otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Người dùng không tồn tại"));

        if (user.getEmailVerified()) {
            throw new InvalidException("Email đã được xác thực");
        }

        if (user.getOtp() == null || !user.getOtp().equals(otp)) {
            throw new InvalidException("OTP không chính xác");
        }
        if (Duration.between(user.getOtpGenerateTime(), LocalDateTime.now()).getSeconds() > (10*60)){
            throw new OtpExpiredException("OTP đã hết hạn, vui lòng gửi lại OTP");
        }
        user.setEmailVerified(true);
        user.setOtp(null);
        userRepository.save(user);

        emailService.sendWelcomeEmail(user.getEmail(), user.getFullName());

        log.info("Email verified for user: {}", email);
    }

    @Override
    public void regenerateOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Người dùng không tồn tại"));

        if (user.getEmailVerified()) {
            throw new InvalidException("Email đã được xác thực");
        }

        String otp = generateOTP();
        user.setOtp(otp);
        user.setOtpGenerateTime(LocalDateTime.now());
        userRepository.save(user);

        emailService.sendOtpEmail(user.getEmail(), otp);
        log.info("OTP regenerated and sent to: {}", email);
    }

    @Override
    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Người dùng không tồn tại"));

        String otp = generateOTP();
        user.setOtp(otp);
        user.setOtpGenerateTime(LocalDateTime.now());
        userRepository.save(user);

        emailService.sendPasswordResetEmail(user.getEmail(), otp);
        log.info("Password reset OTP sent to: {}", email);
    }

    @Override
    public String verifyForgotPassword(String email, String otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Người dùng không tồn tại"));

        if (user.getOtp() == null || !user.getOtp().equals(otp)) {
            throw new InvalidException("OTP không chính xác");
        }
        if (Duration.between(user.getOtpGenerateTime(), LocalDateTime.now()).getSeconds() > (10*60)){
            throw new OtpExpiredException("OTP đã hết hạn, vui lòng gửi lại OTP");
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "password_reset");
        claims.put("userId", user.getId());

        String token = jwtUtils.generateToken(user.getEmail(), claims);

        user.setOtp(null);
        userRepository.save(user);

        log.info("Password reset verified for user: {}", email);
        return token;
    }

    @Override
    public void setPassword(String token, String newPassword, String confirmPassword) {
        try {
            if (!newPassword.equals(confirmPassword)) {
                throw new InvalidException("Mật khẩu không trùng khớp");
            }
            String email = jwtUtils.getUsernameFromToken(token);
            var claims = jwtUtils.getClaimsFromToken(token);
            if (!"password_reset".equals(claims.get("type"))) {
                throw new InvalidException("Token không hợp lệ");
            }

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new NotFoundException("Người dùng không tồn tại"));

            user.setPasswordHash(passwordEncoder.encode(newPassword));
            userRepository.save(user);

            tokenService.revokeAllUserTokens(user.getId());

            log.info("Password reset successfully for user: {}", email);

        } catch (Exception e) {
            log.error("Failed to reset password", e);
            throw new InvalidException("Token không hợp lệ hoặc đã hết hạn");
        }
    }

    @Override
    public TokenResponse refreshToken(String refreshTokenStr, String deviceInfo, String ipAddress) {
        RefreshToken newRefreshToken = tokenService.rotateRefreshToken(refreshTokenStr, deviceInfo, ipAddress);
        User user = newRefreshToken.getUser();

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("roles", user.getRoles().stream()
                .map(Role::getName)
                .toList());

        String accessToken = jwtUtils.generateToken(user.getEmail(), claims);

        log.info("Token refreshed for user: {}", user.getEmail());

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken.getToken())
                .tokenType("Bearer")
                .expiresIn((long) accessTokenExpirationHours * 3600)
                .user(UserResponseDTO.from(user))
                .build();
    }

    @Override
    public void logout(String refreshTokenStr) {
        tokenService.revokeRefreshToken(refreshTokenStr);
        log.info("User logged out successfully");
    }

    @Override
    @Transactional(readOnly = true)
    public long countActiveVerifiedUsers() {
        return userRepository.countActiveVerifiedUsers();
    }


    private String generateOTP() {
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }
}