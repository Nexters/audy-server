package com.pcb.audy.global.util;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pcb.audy.domain.course.repository.CourseRepository;
import com.pcb.audy.domain.pin.repository.PinRepository;
import com.pcb.audy.global.redis.RedisProvider;
import com.pcb.audy.test.PinTest;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PinUtilTest implements PinTest {
    @InjectMocks private PinUtil pinUtil;

    @Mock private RedisProvider redisProvider;
    @Mock private CourseRepository courseRepository;
    @Mock private PinRepository pinRepository;

    @Test
    @DisplayName("disconnect 시 pin 이관 테스트")
    void disconnect_pin_이관() {
        // given
        when(redisProvider.getPinsByPattern(any())).thenReturn(List.of(TEST_SAVED_PIN));
        when(courseRepository.findByCourseId(anyLong())).thenReturn(TEST_COURSE);

        // when
        pinUtil.movePinData(TEST_COURSE_ID);

        // then
        verify(redisProvider).getPinsByPattern(any());
        verify(courseRepository).findByCourseId(anyLong());
        verify(pinRepository).deleteByCourse(any());
        verify(pinRepository).saveAll(any());
        verify(redisProvider).deletePinByPattern(any());
    }
}
