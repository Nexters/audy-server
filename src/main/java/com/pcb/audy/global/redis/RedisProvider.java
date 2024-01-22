package com.pcb.audy.global.redis;

import static java.lang.Boolean.TRUE;

import com.pcb.audy.global.jwt.tokens.RefreshToken;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisProvider {
    private final RedisTemplate<String, Object> redisTemplate;

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void set(String key, Object o, long expireTime) {
        if (hasKey(key)) {
            delete(key);
        }
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(o.getClass()));
        redisTemplate.opsForValue().set(key, o, Duration.ofMillis(expireTime));
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }
}
