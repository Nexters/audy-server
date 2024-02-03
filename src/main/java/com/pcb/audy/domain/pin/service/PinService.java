package com.pcb.audy.domain.pin.service;

import com.pcb.audy.domain.course.entity.Course;
import com.pcb.audy.domain.course.repository.CourseRepository;
import com.pcb.audy.domain.pin.dto.request.PinSaveReq;
import com.pcb.audy.domain.pin.dto.response.PinSaveRes;
import com.pcb.audy.domain.pin.entity.Pin;
import com.pcb.audy.global.redis.RedisProvider;
import com.pcb.audy.global.validator.CourseValidator;
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
                pinSaveReq.getCourseId() + SEPARATOR + pinId,
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

    private Course getCourse(Long courseId) {
        Course course = courseRepository.findByCourseId(courseId);
        CourseValidator.validate(course);
        return course;
    }

    private UUID getPinId() {
        return UUID.randomUUID();
    }
}
