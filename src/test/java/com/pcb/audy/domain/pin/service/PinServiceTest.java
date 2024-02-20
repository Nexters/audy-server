package com.pcb.audy.domain.pin.service;

import static com.pcb.audy.global.response.ResultCode.NOT_FOUND_PIN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pcb.audy.domain.pin.dto.request.PinDeleteReq;
import com.pcb.audy.domain.pin.dto.request.PinNameUpdateReq;
import com.pcb.audy.domain.pin.dto.request.PinSaveReq;
import com.pcb.audy.global.exception.GlobalException;
import com.pcb.audy.global.redis.RedisProvider;
import com.pcb.audy.test.PinTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PinServiceTest implements PinTest {
    @InjectMocks private PinService pinService;

    @Mock private RedisProvider redisProvider;

    @Test
    @DisplayName("pin 저장 테스트")
    void pin_저장() {
        // given
        PinSaveReq pinSaveReq =
                PinSaveReq.builder()
                        .pinName(TEST_PIN_NAME)
                        .originName(TEST_ORIGIN_NAME)
                        .latitude(TEST_LATITUDE)
                        .longitude(TEST_LONGITUDE)
                        .address(TEST_ADDRESS)
                        .sequence(TEST_SEQUENCE)
                        .build();

        // when
        pinService.savePin(TEST_COURSE_ID, pinSaveReq);

        // then
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

    @Nested
    class pin_삭제 {
        @Test
        @DisplayName("pin 삭제 성공 테스트")
        void pin_삭제_성공() {
            // given
            PinDeleteReq pinDeleteReq =
                    PinDeleteReq.builder().courseId(TEST_COURSE_ID).pinId(TEST_PIN_ID).build();
            when(redisProvider.get(any())).thenReturn(TEST_PIN);

            // when
            pinService.deletePin(pinDeleteReq);

            // then
            verify(redisProvider).get(any());
            verify(redisProvider).delete(any());
        }

        @Test
        @DisplayName("pin 삭제 실패 테스트")
        void pin_삭제_실패() {
            // given
            PinDeleteReq pinDeleteReq =
                    PinDeleteReq.builder().courseId(TEST_COURSE_ID).pinId(TEST_PIN_ID).build();
            when(redisProvider.get(any())).thenReturn(null);

            // when
            GlobalException exception =
                    assertThrows(
                            GlobalException.class,
                            () -> {
                                pinService.deletePin(pinDeleteReq);
                            });

            // then
            assertThat(exception.getResultCode()).isEqualTo(NOT_FOUND_PIN);
        }
    }
}
