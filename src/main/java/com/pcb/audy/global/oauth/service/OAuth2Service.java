package com.pcb.audy.global.oauth.service;

import static com.pcb.audy.global.meta.Authority.USER;

import com.pcb.audy.domain.user.entity.User;
import com.pcb.audy.domain.user.repository.UserRepository;
import com.pcb.audy.global.auth.PrincipalDetails;
import com.pcb.audy.global.exception.GlobalException;
import com.pcb.audy.global.meta.Social;
import com.pcb.audy.global.oauth.info.OAuth2UserInfo;
import com.pcb.audy.global.oauth.info.OAuth2UserInfoFactory;
import com.pcb.audy.global.response.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuth2Service extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest)
            throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return process(oAuth2UserRequest, oAuth2User);
        } catch (Exception e) {
            throw new GlobalException(ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    private OAuth2User process(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        Social social =
                Social.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase());
        OAuth2UserInfo oAuth2UserInfo =
                OAuth2UserInfoFactory.getOAuth2UserInfo(social, oAuth2User.getAttributes());
        User user = getUserByOauthId(oAuth2UserInfo, social);

        return PrincipalDetails.builder().user(user).build();
    }

    private User getUserByOauthId(OAuth2UserInfo oAuth2UserInfo, Social social) {
        User user = userRepository.findByOauthId(oAuth2UserInfo.getOauthId());
        if (user == null) {
            return saveUser(oAuth2UserInfo, social);
        }
        return user;
    }

    private User saveUser(OAuth2UserInfo oAuth2UserInfo, Social social) {
        return userRepository.save(
                User.builder()
                        .oauthId(oAuth2UserInfo.getOauthId())
                        .email(oAuth2UserInfo.getEmail())
                        .username(oAuth2UserInfo.getUsername())
                        .authority(USER)
                        .social(social)
                        .imageUrl(oAuth2UserInfo.getImageUrl())
                        .build());
    }
}
