package com.pcb.audy.global.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pcb.audy.domain.pin.dto.response.PinRedisRes;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
@RequiredArgsConstructor
public class RedisProvider {
    private final RedisTemplate<String, Object> redisTemplate;
    private final long PIN_EXPIRE_TIME = Integer.MAX_VALUE;
    private final ObjectMapper objectMapper;

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public <T> T getRedisValue(String key, Class<T> classType) throws JsonProcessingException {
        String redisValue = (String) redisTemplate.opsForValue().get(key);
        if (ObjectUtils.isEmpty(redisValue)) {
            return null;
        } else {
            return objectMapper.readValue(redisValue, classType);
        }
    }

    public <T> List<T> multiGetRedisValue(String pattern, Class<T> classType)
            throws JsonProcessingException {
        Set<String> keys = redisTemplate.keys(pattern);
        // Redis에서 여러 키에 대한 값을 한 번에 조회
        List<Object> redisValues = redisTemplate.opsForValue().multiGet(new ArrayList<>(keys));
        if (ObjectUtils.isEmpty(redisValues)) {
            return List.of();
        } else {
            // 조회된 값들을 각각 지정된 클래스 타입으로 역직렬화
            return redisValues.stream()
                    .filter(redisValue -> !ObjectUtils.isEmpty(redisValue))
                    .map(
                            redisValue -> {
                                try {
                                    return objectMapper.convertValue(redisValue, classType);
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            })
                    .collect(Collectors.toList());
        }
    }

    public List<Object> getValues(String key) {
        Long len = redisTemplate.opsForList().size(key);
        if (len == 0) {
            return List.of();
        }

        return redisTemplate.opsForList().range(key, 0, len - 1);
    }

    public void set(String key, Object o, long expireTime) {
        if (hasKey(key)) {
            delete(key);
        }
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
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
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
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
