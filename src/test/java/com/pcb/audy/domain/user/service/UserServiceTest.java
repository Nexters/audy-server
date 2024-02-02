package com.pcb.audy.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pcb.audy.domain.user.dto.response.UserGetRes;
import com.pcb.audy.domain.user.repository.UserRepository;
import com.pcb.audy.test.UserTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest implements UserTest {
    @InjectMocks private UserService userService;

    @Mock private UserRepository userRepository;

    @Test
    @DisplayName("유저 조회 테스트")
    void 유저_조회() {
        // given
        Long userId = TEST_USER_ID;
        when(userRepository.findByUserId(any())).thenReturn(TEST_USER);

        // when
        UserGetRes userGetRes = userService.getUser(userId);

        // then
        verify(userRepository).findByUserId(any());
        assertThat(userGetRes.getEmail()).isEqualTo(TEST_USER_EMAIL);
        assertThat(userGetRes.getUsername()).isEqualTo(TEST_USER_NAME);
        assertThat(userGetRes.getImageUrl()).isEqualTo(TEST_USER_IMAGE_URL);
    }
}
