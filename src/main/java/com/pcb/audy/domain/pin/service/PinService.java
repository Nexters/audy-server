package com.pcb.audy.domain.pin.service;

import com.pcb.audy.domain.course.entity.Course;
import com.pcb.audy.domain.course.repository.CourseRepository;
import com.pcb.audy.domain.pin.dto.request.PinNameUpdateReq;
import com.pcb.audy.domain.pin.dto.request.PinSaveReq;
import com.pcb.audy.domain.pin.dto.response.PinNameUpdateRes;
import com.pcb.audy.domain.pin.dto.response.PinSaveRes;
import com.pcb.audy.domain.pin.entity.Pin;
import com.pcb.audy.global.redis.RedisProvider;
import com.pcb.audy.global.validator.CourseValidator;
import com.pcb.audy.global.validator.PinValidator;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PinService {
    private final RedisProvider redisProvider;
    private final CourseRepository courseRepository;
    private final String SEPARATOR = ":";

    // TODO fix TTL
    private final long PIN_EXPIRE_TIME = Integer.MAX_VALUE;

    public PinSaveRes savePin(PinSaveReq pinSaveReq) {
        UUID pinId = getPinId();
        redisProvider.set(
                getKey(pinSaveReq.getCourseId(), pinId),
                Pin.builder()
                        .pinId(pinId)
                        .pinName(pinSaveReq.getPinName())
                        .originName(pinSaveReq.getOriginName())
                        .latitude(pinSaveReq.getLatitude())
                        .longitude(pinSaveReq.getLongitude())
                        .address(pinSaveReq.getAddress())
                        .sequence(pinSaveReq.getSequence())
                        .course(getCourse(pinSaveReq.getCourseId()))
                        .build(),
                PIN_EXPIRE_TIME);

        return new PinSaveRes();
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

    private String getKey(Long courseId, UUID pinId) {
        return courseId + SEPARATOR + pinId;
    }

    private Course getCourse(Long courseId) {
        Course course = courseRepository.findByCourseId(courseId);
        CourseValidator.validate(course);
        return course;
    }

    private UUID getPinId() {
        return UUID.randomUUID();
    }
}
