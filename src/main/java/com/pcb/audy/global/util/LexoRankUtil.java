package com.pcb.audy.global.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pravin.raha.lexorank4j.LexoRank;
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

    public String getLexoRank(Long courseId, int order) {
        // 어떤 코스에(courseId), 몇 번째 순서에(order), 어떤 핀을 넣을 것인가(target)
        List<PinRedisRes> pinRedisResList = sortByLexoRank(courseId);

        if (pinRedisResList.isEmpty()) { // 빈 course에 최초 input
            return LexoRank.min().genNext().toString();
        } else if (pinRedisResList.size() != 1 && order == 0) { // 가장 처음으로 이동
            LexoRank nowRank = LexoRank.parse(pinRedisResList.get(0).getSequence());
            return nowRank.genPrev().toString();
        } else if (order == pinRedisResList.size() - 1) { // 가장 마지막에 추가
            LexoRank nowRank = LexoRank.parse(pinRedisResList.get(order).getSequence());
            return nowRank.genNext().toString();
        } else { // 어떤 것의 사이에 들어간다고 했을 때
            LexoRank nowRank = LexoRank.parse(pinRedisResList.get(order).getSequence());
            LexoRank nextRank = LexoRank.parse(pinRedisResList.get(order + 1).getSequence());
            return nowRank.between(nextRank).toString();
        }
    }

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
