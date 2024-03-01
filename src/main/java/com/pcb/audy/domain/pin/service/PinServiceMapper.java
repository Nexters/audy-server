package com.pcb.audy.domain.pin.service;

import com.pcb.audy.domain.course.entity.Course;
import com.pcb.audy.domain.pin.dto.request.PinOrderUpdateReq;
import com.pcb.audy.domain.pin.dto.request.PinSaveReq;
import com.pcb.audy.domain.pin.dto.response.*;
import com.pcb.audy.domain.pin.entity.Pin;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PinServiceMapper {
    PinServiceMapper INSTANCE = Mappers.getMapper(PinServiceMapper.class);

    @Mapping(target = "pinId", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "courseId", source = "courseId")
    @Mapping(target = "sequence", source = "sequence")
    PinRedisRes toPinRedisRes(PinSaveReq pinSaveReq, Long courseId, String sequence);

    PinSaveRes toPinSaveRes(PinRedisRes pinRedisRes);

    PinGetRes toPinGetRes(PinRedisRes pinRedisRes);

    @Mapping(target = "courseId", expression = "java(pin.getCourse().getCourseId())")
    PinRedisRes toPinRedisResFromPin(Pin pin);

    List<PinRedisRes> toPinRedisResListFromPin(List<Pin> pinList);

    List<PinGetRes> toPinGetResListFromPin(List<Pin> pinList);

    List<PinGetRes> toPinGetResListFromRedis(List<PinRedisRes> pinList);

    PinNameUpdateRes toPinNameUpdateRes(PinRedisRes pinRedisRes);

    PinOrderUpdateRes toPinOrderUpdateRes(PinOrderUpdateReq pinOrderUpdateReq);

    PinDeleteRes toPinDeleteRes(PinRedisRes pinRedisRes);

    Pin toPin(PinRedisRes pinRedisRes, Course course);

    default List<Pin> toPinList(List<PinRedisRes> pinRedisResList, Course course) {
        return pinRedisResList.stream().map(pinRedisRes -> toPin(pinRedisRes, course)).toList();
    }
}
