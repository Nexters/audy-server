package com.pcb.audy.domain.pin.service;

import com.pcb.audy.domain.pin.dto.request.PinOrderUpdateReq;
import com.pcb.audy.domain.pin.dto.request.PinSaveReq;
import com.pcb.audy.domain.pin.dto.response.*;
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

    List<PinGetRes> toPinGetResList(List<PinRedisRes> pinList);

    PinNameUpdateRes toPinNameUpdateRes(PinRedisRes pinRedisRes);

    PinOrderUpdateRes toPinOrderUpdateRes(PinOrderUpdateReq pinOrderUpdateReq);

    PinDeleteRes toPinDeleteRes(PinRedisRes pinRedisRes);
}
