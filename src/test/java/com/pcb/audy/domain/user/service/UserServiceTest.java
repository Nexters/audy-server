package com.pcb.audy.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pcb.audy.domain.user.dto.response.UserGetRes;
import com.pcb.audy.domain.user.dto.response.UserGetResList;
import com.pcb.audy.domain.user.repository.UserRepository;
import com.pcb.audy.global.redis.RedisProvider;
import com.pcb.audy.test.CourseTest;
import com.pcb.audy.test.UserTest;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest implements UserTest, CourseTest {
    @InjectMocks private UserService userService;

    @Mock private UserRepository userRepository;
    @Mock private RedisProvider redisProvider;
    @Mock private ObjectMapper objectMapper;

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

    @Test
    @DisplayName("동시 접속 유저 조회 테스트")
    void 동시_접속_유저_조회() {
        // given
        Long courseId = TEST_COURSE_ID;
        List<Object> users = List.of(TEST_USER, TEST_ANOTHER_USER);
        when(redisProvider.getValues(any())).thenReturn(users);

        // when
        UserGetResList userGetResList = userService.getUsers(courseId);

        // then
        assertThat(userGetResList.getTotal()).isEqualTo(2);
        verify(redisProvider).getValues(any());
    }
}
