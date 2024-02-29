package com.pcb.audy.global.redis;

import static java.lang.Boolean.TRUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pcb.audy.test.RedisTest;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@ExtendWith(MockitoExtension.class)
class RedisProviderTest implements RedisTest {
    @InjectMocks private RedisProvider redisProvider;

    @Mock private RedisTemplate<String, Object> redisTemplate;
    @Mock private ValueOperations<String, Object> valueOperations;
    @Mock private ListOperations<String, Object> listOperations;

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

    @Test
    @DisplayName("데이터 조회 테스트")
    void 데이터_조회() {
        // given
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(any())).thenReturn(TEST_VALUE);

        // when
        String value = (String) redisProvider.get(TEST_KEY);

        // then
        verify(redisTemplate).opsForValue();
        verify(valueOperations).get(any());
        assertThat(value).isEqualTo(TEST_VALUE);
    }

    @Test
    @DisplayName("데이터 list 조회 테스트")
    void 데이터_list_조회() {
        // given
        when(redisTemplate.opsForList()).thenReturn(listOperations);
        when(listOperations.size(any())).thenReturn(1L);
        when(listOperations.range(any(), anyLong(), anyLong())).thenReturn(List.of(TEST_VALUE));

        // when
        List<Object> values = redisProvider.getValues(TEST_KEY);

        // then
        verify(redisTemplate, times(2)).opsForList();
        verify(listOperations).size(any());
        verify(listOperations).range(any(), anyLong(), anyLong());
        assertThat(values.size()).isEqualTo(1);
        assertThat(values.get(0)).isEqualTo(TEST_VALUE);
    }

    @Test
    @DisplayName("데이터 삭제 테스트")
    void 데이터_삭제() {
        // given

        // when
        redisProvider.delete(TEST_KEY);

        // then
        verify(redisTemplate).delete(anyString());
    }

    @Test
    @DisplayName("list 데이터 삭제 테스트")
    void list_데이터_삭제() {
        // given
        when(redisTemplate.opsForList()).thenReturn(listOperations);

        // when
        redisProvider.deleteValue(TEST_KEY, TEST_VALUE);

        // then
        verify(redisTemplate).opsForList();
        verify(listOperations).remove(any(), anyLong(), any());
    }

    @Test
    @DisplayName("데이터 존재 확인 테스트")
    void 데이터_존재_확인() {
        // given
        when(redisTemplate.hasKey(any())).thenReturn(TRUE);

        // when
        Boolean result = redisProvider.hasKey(TEST_KEY);

        // then
        verify(redisTemplate).hasKey(any());
        assertThat(result).isEqualTo(TRUE);
    }

    @Test
    @DisplayName("데이터 list에 저장 테스트")
    void 데이터_list_저장() {
        // given
        when(redisTemplate.opsForList()).thenReturn(listOperations);
        when(listOperations.size(any())).thenReturn(0L);
        when(listOperations.remove(any(), anyLong(), any())).thenReturn(1L);

        // when
        redisProvider.setValues(TEST_KEY, TEST_VALUE, TEST_EXPIRE_TIME);

        // then
        verify(redisTemplate, times(3)).opsForList();
        verify(listOperations).size(any());
        verify(listOperations).remove(any(), anyLong(), any());
        verify(listOperations).rightPush(any(), any());
        verify(redisTemplate).expire(any(), anyLong(), any());
    }
}
