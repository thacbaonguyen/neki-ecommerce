package com.thacbao.neki.security.factory;

import com.thacbao.neki.exceptions.ErrorCode;
import com.thacbao.neki.exceptions.common.AppException;
import com.thacbao.neki.security.OAuth2UserInfo;
import com.thacbao.neki.security.info.GithubOAuth2UserInfo;
import com.thacbao.neki.security.info.GoogleOAuth2UserInfo;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if(registrationId.equalsIgnoreCase("google")) {
            return new GoogleOAuth2UserInfo(attributes);
        }
        else if (registrationId.equalsIgnoreCase("github")) {
            return new GithubOAuth2UserInfo(attributes);
        }
        else {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
    }
}
