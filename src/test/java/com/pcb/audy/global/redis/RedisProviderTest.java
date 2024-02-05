package com.pcb.audy.global.redis;

import static java.lang.Boolean.TRUE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pcb.audy.test.RedisTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@ExtendWith(MockitoExtension.class)
class RedisProviderTest implements RedisTest {
    @InjectMocks private RedisProvider redisProvider;

    @Mock private RedisTemplate<String, Object> redisTemplate;
    @Mock private ValueOperations<String, Object> valueOperations;

    @Test
    @DisplayName("데이터 저장 테스트")
    void 데이터_저장() {
        // given
        when(redisTemplate.hasKey(any())).thenReturn(TRUE);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        // when
        redisProvider.set(TEST_KEY, TEST_VALUE, TEST_EXPIRE_TIME);

        // then
        verify(redisTemplate).hasKey(any());
        verify(redisTemplate).delete(anyString());
        verify(redisTemplate).opsForValue();
        verify(valueOperations).set(any(), any(), any());
    }
}
