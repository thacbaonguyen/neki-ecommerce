package com.thacbao.neki.configurations;

import com.thacbao.neki.utils.RateLimitRule;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;

@Component
@RequiredArgsConstructor
@Slf4j
public class RateLimitIPtFilter extends OncePerRequestFilter {
    private final ProxyManager<String> proxyManager;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        String ip = getClientIp(request);

        RateLimitRule rule = resolveRule(path);
        if (rule == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String key = rule.getPrefix() + ip;
        Bucket bucket = proxyManager.builder()
                .build(key, rule::bucketConfig);

        if (!bucket.tryConsume(1)) {
            response.setStatus(429);
            response.setHeader("Retry-After", String.valueOf(rule.getDuration().toSeconds()));
            response.setContentType("application/json");
            response.getWriter().write("""
                {
                  "error": "Quá nhiều request. Vui lòng thử lại sau giây lát"
                }
            """);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private RateLimitRule resolveRule(String path){
        if (path.startsWith("/api/v1/auth/login")) {
            return new RateLimitRule(5, Duration.ofMinutes(1), "login-ip:");
        }

        if (path.startsWith("/api/v1/auth/regenerate-otp")) {
            return new RateLimitRule(3, Duration.ofMinutes(1), "regenerate-ip:");
        }

        if (path.startsWith("/api/v1/auth/forgot-password")) {
            return new RateLimitRule(3, Duration.ofMinutes(5), "forgot-ip:");
        }

        if (path.startsWith("/api/v1/search/products")) {
            return new RateLimitRule(50, Duration.ofMinutes(1), "search-public-ip:");
        }

        if (path.startsWith("/api/v1/products")
                || path.startsWith("/api/v1/categories") || path.startsWith("/api/v1/catalog")) {
            return new RateLimitRule(100, Duration.ofMinutes(1), "public-ip:");
        }


        if (path.startsWith("/api/v1/review/all-review")) {
            return new RateLimitRule(100, Duration.ofMinutes(1), "review-ip:");
        }

        if (path.startsWith("/api/v1/discount")) {
            return new RateLimitRule(100, Duration.ofMinutes(1), "discount-ip:");
        }

        return null;
    }


    private String getClientIp(HttpServletRequest request) {
        String xff = request.getHeader("X-Forwarded-For");
        return (xff != null) ? xff.split(",")[0].trim() : request.getRemoteAddr();
    }
}
