package com.pcb.audy.domain.pin.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pcb.audy.domain.pin.dto.request.PinDeleteReq;
import com.pcb.audy.domain.pin.dto.request.PinNameUpdateReq;
import com.pcb.audy.domain.pin.dto.request.PinSaveReq;
import com.pcb.audy.domain.pin.dto.response.PinDeleteRes;
import com.pcb.audy.domain.pin.dto.response.PinNameUpdateRes;
import com.pcb.audy.domain.pin.dto.response.PinRedisRes;
import com.pcb.audy.domain.pin.dto.response.PinSaveRes;
import com.pcb.audy.global.redis.RedisProvider;
import com.pcb.audy.global.validator.PinValidator;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PinService {
    private final RedisProvider redisProvider;
    private final ObjectMapper objectMapper;
    private final String SEPARATOR = ":";

    // TODO fix TTL
    private final long PIN_EXPIRE_TIME = Integer.MAX_VALUE;

    public PinSaveRes savePin(Long courseId, PinSaveReq pinSaveReq) {
        PinRedisRes pinRedisRes = PinServiceMapper.INSTANCE.toPinRedisRes(pinSaveReq, courseId);
        redisProvider.set(getKey(courseId, pinRedisRes.getPinId()), pinRedisRes, PIN_EXPIRE_TIME);
        return PinServiceMapper.INSTANCE.toPinSaveRes(pinRedisRes);
    }

    public PinNameUpdateRes updatePinName(Long courseId, PinNameUpdateReq pinNameUpdateReq) {
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

    public PinDeleteRes deletePin(PinDeleteReq pinDeleteReq) {
        //        String key = getKey(pinDeleteReq.getCourseId(), pinDeleteReq.getPinId());
        //        Pin pin = (Pin) redisProvider.get(key);
        //                PinValidator.validate(pin);
        //        redisProvider.delete(key);
        return new PinDeleteRes();
    }

    private String getKey(Long courseId, UUID pinId) {
        return courseId + SEPARATOR + pinId;
    }
}
