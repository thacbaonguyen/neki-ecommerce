package com.thacbao.neki.configurations;


import com.thacbao.neki.exceptions.common.AlreadyException;
import com.thacbao.neki.exceptions.common.InvalidException;
import com.thacbao.neki.exceptions.common.NotFoundException;
import com.thacbao.neki.model.Role;
import com.thacbao.neki.model.User;
import com.thacbao.neki.repositories.jpa.RoleRepository;
import com.thacbao.neki.repositories.jpa.UserRepository;
import com.thacbao.neki.security.OAuth2UserInfo;
import com.thacbao.neki.security.UserPrincipal;
import com.thacbao.neki.security.factory.OAuth2UserInfoFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (OAuth2AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, oAuth2User.getAttributes());

//        if (registrationId.equalsIgnoreCase("github")) {
//            oAuth2User.getAttributes().put("email", oAuth2User.getAttributes().get("login") + "@codeSphere.com");
//        }
        if(StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new NotFoundException("Email not found from OAuth2 provider");
        }

        Optional<User> userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());
        User user;
        if(userOptional.isPresent()) {
            user = userOptional.get();

            if (user.getProvider() == null){
                throw new AlreadyException("Your account has been registered by this email");
            }
            // check người dùng đăng nhập với provider khác
            if( !user.getProvider().equals(registrationId)) {
                throw new InvalidException("You're signed up with " +
                        user.getProvider() + ". Please use your " + user.getProvider() + " account to login.");
            }

            user = updateExistingUser(user, oAuth2UserInfo);
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }

        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        try {
            User user = new User();

            user.setProvider(oAuth2UserRequest.getClientRegistration().getRegistrationId());
            user.setProviderId(oAuth2UserInfo.getId());
            user.setFullName(oAuth2UserInfo.getName());
            user.setEmail(oAuth2UserInfo.getEmail());
            user.setIsActive(true);
            user.setEmailVerified(true);

            Role userRole = roleRepository.findByName("USER")
                    .orElseThrow(() -> new NotFoundException("Role USER not found"));
            Set<Role> roles = new HashSet<>();
            roles.add(userRole);
            user.setRoles(roles);

            User savedUser = userRepository.save(user);
            return savedUser;
        }
        catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }


    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setFullName(oAuth2UserInfo.getName());
        return userRepository.save(existingUser);
    }

}
