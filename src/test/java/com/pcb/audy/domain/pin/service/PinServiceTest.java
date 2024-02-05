package com.pcb.audy.domain.pin.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pcb.audy.domain.course.repository.CourseRepository;
import com.pcb.audy.domain.pin.dto.request.PinNameUpdateReq;
import com.pcb.audy.domain.pin.dto.request.PinSaveReq;
import com.pcb.audy.global.redis.RedisProvider;
import com.pcb.audy.test.PinTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PinServiceTest implements PinTest {
    @InjectMocks private PinService pinService;

    @Mock private RedisProvider redisProvider;
    @Mock private CourseRepository courseRepository;

    @Test
    @DisplayName("pin 저장 테스트")
    void pin_저장() {
        // given
        PinSaveReq pinSaveReq =
                PinSaveReq.builder()
                        .courseId(TEST_COURSE_ID)
                        .pinName(TEST_PIN_NAME)
                        .originName(TEST_ORIGIN_NAME)
                        .latitude(TEST_LATITUDE)
                        .longitude(TEST_LONGITUDE)
                        .address(TEST_ADDRESS)
                        .sequence(TEST_SEQUENCE)
                        .build();
        when(courseRepository.findByCourseId(any())).thenReturn(TEST_COURSE);

        // when
        pinService.savePin(pinSaveReq);

        // then
        verify(courseRepository).findByCourseId(any());
        verify(redisProvider).set(any(), any(), anyLong());
    }

    @Test
    @DisplayName("pin 이름 수정 테스트")
    void pin_이름_수정() {
        // given
        PinNameUpdateReq pinNameUpdateReq =
                PinNameUpdateReq.builder()
                        .courseId(TEST_COURSE_ID)
                        .pinId(TEST_PIN_ID)
                        .pinName(TEST_UPDATED_PIN_NAME)
                        .build();
        when(redisProvider.get(any())).thenReturn(TEST_PIN);

        // when
        pinService.updatePinName(pinNameUpdateReq);

        // then
        verify(redisProvider).get(any());
        verify(redisProvider).set(any(), any(), anyLong());
    }
}
