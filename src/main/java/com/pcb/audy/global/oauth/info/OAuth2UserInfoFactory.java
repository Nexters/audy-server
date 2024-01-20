package com.pcb.audy.global.oauth.info;

import static com.pcb.audy.global.meta.Social.KAKAO;

import com.pcb.audy.global.meta.Social;
import com.pcb.audy.global.oauth.info.impl.KakaoOAuth2UserInfo;
import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(Social social, Map<String, Object> attributes) {
        if (KAKAO.equals(social)) {
            return new KakaoOAuth2UserInfo(attributes);
        }

        // TODO add APPLE
        // TODO fix to GlobalException
        throw new IllegalArgumentException();
    }
}
