package com.pcb.audy.domain.pin.service;

import com.pcb.audy.domain.pin.dto.request.PinSaveReq;
import com.pcb.audy.domain.pin.dto.response.PinDataRes;
import com.pcb.audy.domain.pin.dto.response.PinSaveRes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PinServiceMapper {
    PinServiceMapper INSTANCE = Mappers.getMapper(PinServiceMapper.class);

    PinSaveRes toPinSaveRes(PinDataRes pinDataRes);

    @Mapping(target = "pinId", expression = "java(java.util.UUID.randomUUID())")
    PinDataRes toPinDataRes(PinSaveReq pinSaveReq);
}
