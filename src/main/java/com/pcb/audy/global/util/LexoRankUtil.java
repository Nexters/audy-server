package com.pcb.audy.global.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pcb.audy.domain.pin.dto.response.PinRedisRes;
import com.pcb.audy.global.redis.RedisProvider;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LexoRankUtil {

    private final RedisProvider redisProvider;
    private final ObjectMapper objectMapper;

    public List<PinRedisRes> sortByLexoRank(Long courseId) {
        String pattern = courseId + ":*";
        List<Object> redisData = redisProvider.getByPattern(pattern);

        List<PinRedisRes> pinResList =
                new java.util.ArrayList<>(
                        redisData.stream()
                                .map(pin -> objectMapper.convertValue(pin, PinRedisRes.class))
                                .toList());

        Collections.sort(pinResList);
        return pinResList;
    }
}
