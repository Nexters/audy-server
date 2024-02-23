package com.pcb.audy.domain.pin.service;

import com.pcb.audy.domain.pin.dto.request.PinSaveReq;
import com.pcb.audy.domain.pin.dto.response.PinDeleteRes;
import com.pcb.audy.domain.pin.dto.response.PinNameUpdateRes;
import com.pcb.audy.domain.pin.dto.response.PinRedisRes;
import com.pcb.audy.domain.pin.dto.response.PinSaveRes;
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

    PinNameUpdateRes toPinNameUpdateRes(PinRedisRes pinRedisRes);

    PinDeleteRes toPinDeleteRes(PinRedisRes pinRedisRes);
}
