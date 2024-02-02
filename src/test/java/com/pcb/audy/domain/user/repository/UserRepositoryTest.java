package com.pcb.audy.domain.user.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.pcb.audy.domain.user.entity.User;
import com.pcb.audy.test.UserTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest implements UserTest {
    @Autowired private UserRepository userRepository;

    @Test
    @DisplayName("oauthId로 유저 조회 테스트")
    void oauthId_유저_조회() {
        // given
        User savedUser = userRepository.save(TEST_USER);

        // when
        User user = userRepository.findByOauthId(TEST_OAUTH_ID);

        // then
        assertThat(user).isEqualTo(savedUser);
    }

    @Test
    @DisplayName("email로 유저 조회 테스트")
    void email_유저_조회() {
        // given
        User savedUser = userRepository.save(TEST_USER);

        // when
        User user = userRepository.findByEmail(TEST_USER_EMAIL);

        // then
        assertThat(user).isEqualTo(savedUser);
    }

    @Test
    @DisplayName("userId로 유저 조회 테스트")
    void userId_유저_조회() {
        // given
        User savedUser = userRepository.save(TEST_USER);

        // when
        User user = userRepository.findByUserId(savedUser.getUserId());

        // then
        assertThat(user).isEqualTo(savedUser);
    }
}
