package com.thacbao.neki.configurations;

import com.thacbao.neki.security.UserPrincipal;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;
    Claims claims = null;
    String email = null;
    private static final String[] PUBLIC_PATHS = {
            "/api/v1/auth/login",
            "/api/v1/auth/signup",
            "/api/v1/auth/forgot-password",
            "/api/v1/auth/verify-account",
            "/api/v1/auth/regenerate-otp",
            "/api/v1/auth/test",
            "/api/v1/auth/set-password.*",
            "/api/v1/auth/verify-forgot-password",
            "/oauth2/callback.*"
    };

    private Boolean isPublicPath(String path){
        return Arrays.stream(PUBLIC_PATHS).anyMatch(path::matches);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        log.info(path);
        if (isPublicPath(request.getServletPath())){
            filterChain.doFilter(request, response);
        }
        else if (path.equals("/api/v1/auth/refresh-token")){
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                String username = jwtUtils.getUserNameFromTokenExpired(token);
                if (username != null) {
                    log.info("refresh token : {}", username);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    UsernamePasswordAuthenticationToken authentication = new
                            UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            filterChain.doFilter(request, response);
        }
        else{
            String authorizationHeader = request.getHeader("Authorization");
            String token = null;
            if (authorizationHeader != null){
                if(authorizationHeader.startsWith("Bearer ")){
                    token = authorizationHeader.substring(7);
                    try {
                        claims = jwtUtils.getClaimsFromToken(token);
                        email = jwtUtils.getUsernameFromToken(token);
                    } catch (Exception e) {
                        log.error("Invalid JWT token: {}", e.getMessage());
                    }
                }
            }
            if(claims != null && email != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                if (userDetails instanceof UserPrincipal userPrincipal) {
                    // Check if email is not verified
                    if (!userPrincipal.getEmailVerified()) {
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        response.setContentType("application/json");
                        response.getWriter().write(
                                "{\"message\":\"Please verify your email first\",\"status\":\"UNVERIFIED\"}"
                        );
                        return;
                    }
                }
                if(jwtUtils.validateToken(token, userDetails)){
                    UsernamePasswordAuthenticationToken authentication = new
                            UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            filterChain.doFilter(request, response);
        }
    }

    public Boolean isAdmin(){
        if (claims == null) return false;
        List<String> roles = claims.get("role", List.class);
        return roles.contains("admin".toUpperCase());
    }

    public Boolean isUser(){
        if (claims == null) return false;
        List<String> roles = claims.get("role", List.class);
        return roles.contains("user".toUpperCase());
    }

    public String getCurrentUsername(){
        return email;
    }
}
