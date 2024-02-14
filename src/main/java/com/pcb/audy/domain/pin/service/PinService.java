package com.pcb.audy.domain.pin.service;

import com.pcb.audy.domain.pin.dto.request.PinDeleteReq;
import com.pcb.audy.domain.pin.dto.request.PinNameUpdateReq;
import com.pcb.audy.domain.pin.dto.request.PinSaveReq;
import com.pcb.audy.domain.pin.dto.response.PinDataRes;
import com.pcb.audy.domain.pin.dto.response.PinDeleteRes;
import com.pcb.audy.domain.pin.dto.response.PinNameUpdateRes;
import com.pcb.audy.domain.pin.dto.response.PinSaveRes;
import com.pcb.audy.domain.pin.entity.Pin;
import com.pcb.audy.global.redis.RedisProvider;
import com.pcb.audy.global.validator.PinValidator;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PinService {
    private final RedisProvider redisProvider;
    private final String SEPARATOR = ":";

    // TODO fix TTL
    private final long PIN_EXPIRE_TIME = Integer.MAX_VALUE;

    public PinSaveRes savePin(PinSaveReq pinSaveReq) {
        PinDataRes pinDataRes = PinServiceMapper.INSTANCE.toPinDataRes(pinSaveReq);
        redisProvider.set(
                getKey(pinSaveReq.getCourseId(), pinDataRes.getPinId()), pinDataRes, PIN_EXPIRE_TIME);
        return PinServiceMapper.INSTANCE.toPinSaveRes(pinDataRes);
    }

    public PinNameUpdateRes updatePinName(PinNameUpdateReq pinNameUpdateReq) {
        String key = getKey(pinNameUpdateReq.getCourseId(), pinNameUpdateReq.getPinId());
        Pin prevPin = (Pin) redisProvider.get(key);
        PinValidator.validate(prevPin);
        redisProvider.set(
                key,
                Pin.builder()
                        .pinId(pinNameUpdateReq.getPinId())
                        .pinName(pinNameUpdateReq.getPinName())
                        .originName(prevPin.getOriginName())
                        .latitude(prevPin.getLatitude())
                        .longitude(prevPin.getLongitude())
                        .address(prevPin.getAddress())
                        .sequence(prevPin.getSequence())
                        .course(prevPin.getCourse())
                        .build(),
                PIN_EXPIRE_TIME);

        return new PinNameUpdateRes();
    }

    public PinDeleteRes deletePin(PinDeleteReq pinDeleteReq) {
        String key = getKey(pinDeleteReq.getCourseId(), pinDeleteReq.getPinId());
        Pin pin = (Pin) redisProvider.get(key);
        PinValidator.validate(pin);
        redisProvider.delete(key);
        return new PinDeleteRes();
    }

    private String getKey(Long courseId, UUID pinId) {
        return courseId + SEPARATOR + pinId;
    }
}
