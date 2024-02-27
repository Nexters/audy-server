package com.pcb.audy.global.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pravin.raha.lexorank4j.LexoRank;
import com.pcb.audy.domain.pin.dto.response.PinRedisRes;
import com.pcb.audy.global.redis.RedisProvider;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
@RequiredArgsConstructor
public class LexoRankUtil {

    private final RedisProvider redisProvider;
    private final ObjectMapper objectMapper;

    public String getLexoRank(Long courseId, int order) {
        // 어떤 코스에(courseId), 몇 번째 순서에(order), 어떤 핀을 넣을 것인가(target)
        List<PinRedisRes> pinRedisResList = sortByLexoRank(courseId);
        if (CollectionUtils.isEmpty(pinRedisResList)) { // 빈 course에 최초 input
            return LexoRank.min().genNext().toString();
        } else if (pinRedisResList.size() != 0 && order == 0) { // 가장 처음으로 이동
            LexoRank nowRank = LexoRank.parse(pinRedisResList.get(0).getSequence());
            return nowRank.genPrev().toString();
        } else if (order == pinRedisResList.size()) { // save를 통해서 마지막에 추가
            LexoRank nowRank = LexoRank.parse(pinRedisResList.get(order - 1).getSequence());
            return nowRank.genNext().toString();
        } else if (order == pinRedisResList.size() - 1) { // update Order를 통해서 가장 마지막에 추가
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

        if (redisData == null) {
            return List.of();
        }

        List<PinRedisRes> pinResList = new ArrayList<>();
        for (Object pin : redisData) {
            System.out.println(pin.toString());
            PinRedisRes pinRedisRes = objectMapper.convertValue(pin, PinRedisRes.class);
            System.out.println(pinRedisRes.toString());
            System.out.println(pinRedisRes.getPinId());
            pinResList.add(pinRedisRes);
            System.out.println("---------------");
        }

        System.out.println("add finsihed");
        Collections.sort(pinResList);
        System.out.println("sorting finished");
        return pinResList;
    }
}
