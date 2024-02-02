package com.pcb.audy.test;

import static com.pcb.audy.global.meta.Authority.USER;
import static com.pcb.audy.global.meta.Social.KAKAO;

import com.pcb.audy.domain.user.entity.User;

public interface UserTest {

    Long TEST_USER_ID = 1L;
    String TEST_OAUTH_ID = "oauthId";
    String TEST_USER_NAME = "username";
    String TEST_USER_EMAIL = "username@gmail.com";
    String TEST_USER_IMAGE_URL = "imageUrl";

    User TEST_USER =
            User.builder()
                    .userId(TEST_USER_ID)
                    .oauthId(TEST_OAUTH_ID)
                    .email(TEST_USER_EMAIL)
                    .username(TEST_USER_NAME)
                    .authority(USER)
                    .social(KAKAO)
                    .imageUrl(TEST_USER_IMAGE_URL)
                    .build();
}
