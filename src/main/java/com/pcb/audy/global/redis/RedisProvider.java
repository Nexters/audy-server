package com.pcb.audy.global.redis;

import com.pcb.audy.domain.pin.dto.response.PinRedisRes;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
@RequiredArgsConstructor
public class RedisProvider {
    private final RedisTemplate<String, Object> redisTemplate;
    private final long PIN_EXPIRE_TIME = Integer.MAX_VALUE;

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public List<Object> getByPattern(String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        if (CollectionUtils.isEmpty(keys)) {
            return List.of();
        }
        return redisTemplate.opsForValue().multiGet(keys);
    }

    public void set(String key, Object o, long expireTime) {
        if (hasKey(key)) {
            delete(key);
        }
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(o.getClass()));
        redisTemplate.opsForValue().set(key, o, Duration.ofMillis(expireTime));
    }

    public void multiSet(List<PinRedisRes> pinSaveResList) {
        redisTemplate.executePipelined(
                (RedisCallback<Object>)
                        connection -> {
                            // JSON 직렬화를 위한 설정
                            RedisSerializer<String> stringSerializer = redisTemplate.getStringSerializer();
                            RedisSerializer<PinRedisRes> valueSerializer =
                                    new Jackson2JsonRedisSerializer<>(PinRedisRes.class);

                            pinSaveResList.forEach(
                                    pinRedisRes -> {
                                        // courseId와 pinId를 조합하여 키 생성
                                        String key = pinRedisRes.getCourseId() + ":" + pinRedisRes.getPinId();
                                        byte[] serializedKey = stringSerializer.serialize(key);
                                        byte[] serializedValue = valueSerializer.serialize(pinRedisRes);

                                        // Redis에 키-값 쌍 저장
                                        connection.set(
                                                serializedKey,
                                                serializedValue,
                                                Expiration.milliseconds(PIN_EXPIRE_TIME),
                                                RedisStringCommands.SetOption.UPSERT);
                                    });
                            return null; // 파이프라인 내에서 각 개별 명령의 결과 처리를 무시하겠다
                        });
    }

    public void setValues(String key, Object o, long expireTime) {
        Long len = redisTemplate.opsForList().size(key);
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(o.getClass()));
        redisTemplate.opsForList().remove(key, 1L, o);
        redisTemplate.opsForList().rightPush(key, o);

        if (len == 0) {
            redisTemplate.expire(key, expireTime, TimeUnit.MILLISECONDS);
        }
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }
}
