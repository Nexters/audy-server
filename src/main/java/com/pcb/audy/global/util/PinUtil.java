package com.pcb.audy.global.util;

import com.pcb.audy.domain.course.entity.Course;
import com.pcb.audy.domain.course.repository.CourseRepository;
import com.pcb.audy.domain.pin.dto.response.PinRedisRes;
import com.pcb.audy.domain.pin.repository.PinRepository;
import com.pcb.audy.domain.pin.service.PinServiceMapper;
import com.pcb.audy.global.redis.RedisProvider;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Component
@RequiredArgsConstructor
public class PinUtil {
    private final RedisProvider redisProvider;
    private final CourseRepository courseRepository;
    private final PinRepository pinRepository;

    @Transactional
    public void movePinData(Long courseId) {
        String pattern = getCourseKeyPattern(courseId);
        List<PinRedisRes> pinRedisResList = redisProvider.getPinsByPattern(pattern);
        if (CollectionUtils.isEmpty(pinRedisResList)) {
            return;
        }

        Course course = courseRepository.findByCourseId(courseId);
        pinRepository.deleteByCourse(course);
        pinRepository.saveAll(PinServiceMapper.INSTANCE.toPinList(pinRedisResList, course));
        redisProvider.deletePinByPattern(pattern);
    }

    private String getCourseKeyPattern(Long courseId) {
        return courseId + ":*";
    }
}
