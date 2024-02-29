package com.pcb.audy.domain.pin.service;

import static com.pcb.audy.global.response.ResultCode.FAILED_DECRYPT;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pcb.audy.domain.pin.dto.request.PinDeleteReq;
import com.pcb.audy.domain.pin.dto.request.PinNameUpdateReq;
import com.pcb.audy.domain.pin.dto.request.PinOrderUpdateReq;
import com.pcb.audy.domain.pin.dto.request.PinSaveReq;
import com.pcb.audy.domain.pin.dto.response.*;
import com.pcb.audy.global.exception.GlobalException;
import com.pcb.audy.global.redis.RedisProvider;
import com.pcb.audy.global.util.LexoRankUtil;
import com.pcb.audy.global.validator.PinValidator;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PinService {
    private final RedisProvider redisProvider;
    private final ObjectMapper objectMapper;
    private final LexoRankUtil lexoRankUtil;
    private final String SEPARATOR = ":";

    // TODO fix TTL
    private final long PIN_EXPIRE_TIME = Integer.MAX_VALUE;

    public PinSaveRes savePin(Long courseId, PinSaveReq pinSaveReq) {
        PinValidator.validateName(pinSaveReq.getPinName());
        try {
            int size = redisProvider.multiGetRedisValue(courseId + ":*", PinGetRes.class).size();
            isExceedPinLimit(size);

            String sequence = lexoRankUtil.getLexoRank(courseId, size);
            PinRedisRes pinRedisRes =
                    PinServiceMapper.INSTANCE.toPinRedisRes(pinSaveReq, courseId, sequence);
            redisProvider.set(getKey(courseId, pinRedisRes.getPinId()), pinRedisRes, PIN_EXPIRE_TIME);
            return PinServiceMapper.INSTANCE.toPinSaveRes(pinRedisRes);
        } catch (Exception e) {
            throw new GlobalException(FAILED_DECRYPT);
        }
    }

    public PinOrderUpdateRes updatePinOrder(Long courseId, PinOrderUpdateReq pinOrderUpdateReq) {
        String key = getKey(courseId, pinOrderUpdateReq.getPinId());
        String sequence = lexoRankUtil.getLexoRank(courseId, pinOrderUpdateReq.getOrder());

        PinRedisRes pinRedisRes = objectMapper.convertValue(redisProvider.get(key), PinRedisRes.class);
        PinRedisRes updatedPinRedisRes =
                PinRedisRes.builder()
                        .courseId(pinRedisRes.getCourseId())
                        .pinId(pinRedisRes.getPinId())
                        .pinName(pinRedisRes.getPinName())
                        .originName(pinRedisRes.getOriginName())
                        .latitude(pinRedisRes.getLatitude())
                        .longitude(pinRedisRes.getLongitude())
                        .address(pinRedisRes.getAddress())
                        .sequence(sequence)
                        .build();

        redisProvider.set(key, updatedPinRedisRes, PIN_EXPIRE_TIME);
        return PinServiceMapper.INSTANCE.toPinOrderUpdateRes(pinOrderUpdateReq);
    }

    public PinNameUpdateRes updatePinName(Long courseId, PinNameUpdateReq pinNameUpdateReq) {
        PinValidator.validateName(pinNameUpdateReq.getPinName());
        String key = getKey(courseId, pinNameUpdateReq.getPinId());
        PinRedisRes prevPin = objectMapper.convertValue(redisProvider.get(key), PinRedisRes.class);
        PinValidator.validate(prevPin);
        PinRedisRes updatedPin =
                PinRedisRes.builder()
                        .courseId(prevPin.getCourseId())
                        .pinId(pinNameUpdateReq.getPinId())
                        .pinName(pinNameUpdateReq.getPinName())
                        .originName(prevPin.getOriginName())
                        .latitude(prevPin.getLatitude())
                        .longitude(prevPin.getLongitude())
                        .address(prevPin.getAddress())
                        .sequence(prevPin.getSequence())
                        .build();
        redisProvider.set(key, updatedPin, PIN_EXPIRE_TIME);
        return PinServiceMapper.INSTANCE.toPinNameUpdateRes(updatedPin);
    }

    public PinDeleteRes deletePin(Long courseId, PinDeleteReq pinDeleteReq) {
        String key = getKey(courseId, pinDeleteReq.getPinId());
        PinRedisRes pinRedisRes = objectMapper.convertValue(redisProvider.get(key), PinRedisRes.class);
        PinValidator.validate(pinRedisRes);
        redisProvider.delete(key);
        return PinServiceMapper.INSTANCE.toPinDeleteRes(pinRedisRes);
    }

    private String getKey(Long courseId, UUID pinId) {
        return courseId + SEPARATOR + pinId;
    }

    private void isExceedPinLimit(int size) {
        PinValidator.checkIsExceedPinLimit(size);
    }
}
