package com.pcb.audy.domain.pin.service;

import com.pcb.audy.domain.pin.dto.request.PinSaveReq;
import com.pcb.audy.domain.pin.dto.response.PinRedisRes;
import com.pcb.audy.domain.pin.dto.response.PinSaveRes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PinServiceMapper {
    PinServiceMapper INSTANCE = Mappers.getMapper(PinServiceMapper.class);

    @Mapping(target = "pinId", expression = "java(java.util.UUID.randomUUID())")
    PinRedisRes toPinRedisRes(PinSaveReq pinSaveReq, Long courseId);

    PinSaveRes toPinSaveRes(PinRedisRes pinRedisRes);
}
