package com.thacbao.neki.security.handler;

import com.thacbao.neki.configurations.JwtUtils;
import com.thacbao.neki.exceptions.ErrorCode;
import com.thacbao.neki.exceptions.common.AppException;
import com.thacbao.neki.exceptions.common.NotFoundException;
import com.thacbao.neki.model.Role;
import com.thacbao.neki.model.User;
import com.thacbao.neki.repositories.jpa.UserRepository;
import com.thacbao.neki.security.UserPrincipal;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
@Slf4j
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    @Value("${app.oauth2.redirectUrl}")
    private String authorizedRedirectUri;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        String targetUrl = determineTargetUrl(authentication);

        if (response.isCommitted()) {
            log.info("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        User user = userRepository.findByEmail(principal.getEmail()).orElseThrow(
                () -> new NotFoundException("Cannot find user " + principal.getEmail())
        );
        try {
            Set<Role> roles = user.getRoles();
            Map<String, Object> claim = new HashMap<>();
            claim.put("role", roles);
            claim.put("email", principal.getEmail());

            String token = jwtUtils.generateToken(user.getEmail(), claim);

            return UriComponentsBuilder.fromUriString(authorizedRedirectUri)
                    .queryParam("token", token)
                    .build().toUriString();
        }
        catch (Exception e) {
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
