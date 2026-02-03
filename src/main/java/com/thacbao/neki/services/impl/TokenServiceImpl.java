package com.thacbao.neki.services.impl;

import com.thacbao.neki.exceptions.common.InvalidException;
import com.thacbao.neki.exceptions.common.NotFoundException;
import com.thacbao.neki.model.RefreshToken;
import com.thacbao.neki.model.User;
import com.thacbao.neki.repositories.jpa.RefreshTokenRepository;
import com.thacbao.neki.repositories.jpa.UserRepository;
import com.thacbao.neki.services.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TokenServiceImpl implements TokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Value("${jwt.refreshToken.expirationDays:7}")
    private int refreshTokenExpirationDays;

    @Value("${jwt.refreshToken.maxDevices:5}")
    private int maxDevicesPerUser;

    /**
     * tao refresh token sau khi dang nhap
     * */
    @Override
    public RefreshToken createRefreshToken(User user, String deviceInfo, String ipAddress) {
        // Check max devices limit
        long validTokenCount = refreshTokenRepository.countValidTokensByUser(user, LocalDateTime.now());
        if (validTokenCount >= maxDevicesPerUser) {
            // Revoke oldest token
            List<RefreshToken> userTokens = refreshTokenRepository.findByUser(user);
            userTokens.stream()
                    .filter(RefreshToken::isValid)
                    .min((t1, t2) -> t1.getCreatedAt().compareTo(t2.getCreatedAt()))
                    .ifPresent(oldestToken -> {
                        oldestToken.setIsRevoked(true);
                        oldestToken.setRevokedAt(LocalDateTime.now());
                        refreshTokenRepository.save(oldestToken);
                        log.info("Revoked oldest token for user {} due to device limit", user.getEmail());
                    });
        }

        // Create new refresh token
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(generateRefreshToken())
                .expiresAt(LocalDateTime.now().plusDays(refreshTokenExpirationDays))
                .isRevoked(false)
                .deviceInfo(deviceInfo)
                .ipAddress(ipAddress)
                .build();

        refreshTokenRepository.save(refreshToken);
        log.info("Created refresh token for user: {}", user.getEmail());

        return refreshToken;
    }

    /**
     * kiem tra refesh token da bi thu hoi hoawc het han chua
     * */
    @Override
    @Transactional(readOnly = true)
    public RefreshToken verifyRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidException("Refresh token không hợp lệ"));

        if (!refreshToken.isValid()) {
            if (refreshToken.getIsRevoked()) {
                throw new InvalidException("Refresh token đã bị thu hồi");
            } else if (refreshToken.isExpired()) {
                throw new InvalidException("Refresh token đã hết hạn");
            }
        }

        return refreshToken;
    }

    /**
     * thu hoi rf tolen sau khi logout
     * */
    @Override
    public void revokeRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new NotFoundException("Refresh token không tồn tại"));

        refreshToken.setIsRevoked(true);
        refreshToken.setRevokedAt(LocalDateTime.now());
        refreshTokenRepository.save(refreshToken);

        log.info("Revoked refresh token for user: {}", refreshToken.getUser().getEmail());
    }

    /**
     * thu hoi tat ca rf token sau khi doi passw hoac bi block
     * */
    @Override
    public void revokeAllUserTokens(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Người dùng không tồn tại"));

        List<RefreshToken> userTokens = refreshTokenRepository.findByUser(user);
        userTokens.forEach(token -> {
            token.setIsRevoked(true);
            token.setRevokedAt(LocalDateTime.now());
        });

        refreshTokenRepository.saveAll(userTokens);
        log.info("Revoked all tokens for user: {}", user.getEmail());
    }

    /**
     * thu hoi token cu va tao tk va rf tk moi
     * */
    @Override
    public RefreshToken rotateRefreshToken(String oldToken, String deviceInfo, String ipAddress) {
        // Verify old token
        RefreshToken oldRefreshToken = verifyRefreshToken(oldToken);
        User user = oldRefreshToken.getUser();

        // Revoke old token
        oldRefreshToken.setIsRevoked(true);
        oldRefreshToken.setRevokedAt(LocalDateTime.now());
        refreshTokenRepository.save(oldRefreshToken);

        // Create new token
        RefreshToken newRefreshToken = createRefreshToken(user, deviceInfo, ipAddress);

        log.info("Rotated refresh token for user: {}", user.getEmail());
        return newRefreshToken;
    }

    @Override
    @Scheduled(cron = "0 0 2 * * ?") // kiem tra dinh ky
    public void cleanupExpiredTokens() {
        log.info("Starting cleanup of expired and revoked tokens");
        refreshTokenRepository.deleteExpiredAndRevokedTokens(LocalDateTime.now());
        log.info("Cleanup completed");
    }

    private String generateRefreshToken() {
        return UUID.randomUUID().toString() + "-" + UUID.randomUUID().toString();
    }
}