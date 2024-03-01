package com.pcb.audy.global.util;

import com.github.pravin.raha.lexorank4j.LexoRank;
import com.pcb.audy.domain.pin.dto.response.PinGetRes;
import com.pcb.audy.domain.pin.dto.response.PinRedisRes;
import com.pcb.audy.domain.pin.service.PinServiceMapper;
import com.pcb.audy.global.redis.RedisProvider;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class LexoRankUtil {

    private final RedisProvider redisProvider;

    public String getLexoRank(Long courseId, int order) {
        // 어떤 코스에(courseId), 몇 번째 순서에(order), 어떤 핀을 넣을 것인가(target)
        List<PinGetRes> pinGetResList = sortByLexoRank(courseId);
        if (CollectionUtils.isEmpty(pinGetResList)) { // 빈 course에 최초 input
            return LexoRank.min().genNext().toString();
        } else if (pinGetResList.size() != 0 && order == 0) { // 가장 처음으로 이동
            LexoRank nowRank = LexoRank.parse(pinGetResList.get(0).getSequence());
            return nowRank.genPrev().toString();
        } else if (order == pinGetResList.size()) { // save를 통해서 마지막에 추가
            LexoRank nowRank = LexoRank.parse(pinGetResList.get(order - 1).getSequence());
            return nowRank.genNext().toString();
        } else if (order == pinGetResList.size() - 1) { // update Order를 통해서 가장 마지막에 추가
            LexoRank nowRank = LexoRank.parse(pinGetResList.get(order).getSequence());
            return nowRank.genNext().toString();
        } else { // 어떤 것의 사이에 들어간다고 했을 때
            LexoRank nowRank = LexoRank.parse(pinGetResList.get(order).getSequence());
            LexoRank nextRank = LexoRank.parse(pinGetResList.get(order + 1).getSequence());
            return nowRank.between(nextRank).toString();
        }
    }

    public List<PinGetRes> sortByLexoRank(Long courseId) {
        String pattern = courseId + ":*";
        List<PinRedisRes> redisData = redisProvider.getPinsByPattern(pattern);

        if (CollectionUtils.isEmpty(redisData)) {
            return List.of();
        }
        Collections.sort(redisData);
        return PinServiceMapper.INSTANCE.toPinGetResListFromRedis(redisData);
    }
}
