package com.pcb.audy.domain.sample.service;

import com.pcb.audy.domain.sample.dto.response.SampleGetRes;
import com.pcb.audy.domain.sample.dto.response.SampleSaveRes;
import com.pcb.audy.domain.sample.entity.Sample;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SampleServiceMapper {

    SampleServiceMapper INSTANCE = Mappers.getMapper(SampleServiceMapper.class);

    SampleSaveRes toSampleSaveRes(Sample sampleEntity);

    SampleGetRes toSampleGetRes(Sample sampleEntity);

    List<SampleGetRes> toSampleGetReses(List<Sample> sampleEntities);
}
