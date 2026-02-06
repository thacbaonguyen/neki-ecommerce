package com.thacbao.neki.configurations;

import com.thacbao.neki.security.UserPrincipal;
import com.thacbao.neki.utils.RateLimitRule;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
@Slf4j
public class RateLimitUserFilter extends OncePerRequestFilter {

    private final ProxyManager<String> proxyManager;

    private final String[] AUTH_USER_LIMIT = {
            "/api/v1/auth/test",
            "/api/v1/auth/refresh-token",
            "/api/v1/auth/change-password",
            "/api/v1/auth/logout",
    };

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof UserPrincipal principal)) {
            filterChain.doFilter(request, response);
            return;
        }

        String path = request.getRequestURI();
        RateLimitRule rule = resolveRule(path);

        if (rule == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String key = rule.getPrefix() + principal.getId();

        Bucket bucket = proxyManager.builder()
                .build(key, rule::bucketConfig);

        if (!bucket.tryConsume(1)) {
            response.setStatus(429);
            response.setHeader("Retry-After", String.valueOf(rule.getDuration().toSeconds()));
            response.setContentType("application/json");
            response.getWriter().write("""
                {
                  "error": "Quá nhiều request cho tài nguyên này. Vui lòng thủ lại sau"
                }
            """);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private RateLimitRule resolveRule(String path) {

        if (path.startsWith("/api/v1/orders")
                || path.startsWith("/api/v1/checkout")) {
            return new RateLimitRule(10, Duration.ofMinutes(1), "order-user:");
        }

        if (Arrays.stream(AUTH_USER_LIMIT).anyMatch(path::startsWith)) {
            return new RateLimitRule(5, Duration.ofMinutes(1), "auth-user:");
        }

        if (path.startsWith("/api/v1/auth/users")) {
            return new RateLimitRule(100, Duration.ofMinutes(1), "auth-admin:");
        }

        if (path.startsWith("/api/v1/admin")) {
            return new RateLimitRule(500, Duration.ofMinutes(1), "admin-user:");
        }

        if (path.startsWith("/api/v1/auth/profile")) {
            return new RateLimitRule(30, Duration.ofMinutes(1), "profile-user:");
        }

        if (path.startsWith("/api/v1/wishlist")) {
            return new RateLimitRule(30, Duration.ofMinutes(1), "wishlist-user:");
        }

        return null;
    }
}