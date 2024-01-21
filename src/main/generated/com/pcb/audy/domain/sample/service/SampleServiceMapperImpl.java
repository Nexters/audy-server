package com.pcb.audy.domain.sample.service;

import com.pcb.audy.domain.sample.dto.response.SampleGetRes;
import com.pcb.audy.domain.sample.dto.response.SampleSaveRes;
import com.pcb.audy.domain.sample.entity.Sample;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-01-21T21:43:04+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.9 (Homebrew)"
)
public class SampleServiceMapperImpl implements SampleServiceMapper {

    @Override
    public SampleSaveRes toSampleSaveRes(Sample sampleEntity) {
        if ( sampleEntity == null ) {
            return null;
        }

        SampleSaveRes.SampleSaveResBuilder sampleSaveRes = SampleSaveRes.builder();

        sampleSaveRes.sampleId( sampleEntity.getSampleId() );

        return sampleSaveRes.build();
    }

    @Override
    public SampleGetRes toSampleGetRes(Sample sampleEntity) {
        if ( sampleEntity == null ) {
            return null;
        }

        SampleGetRes.SampleGetResBuilder sampleGetRes = SampleGetRes.builder();

        sampleGetRes.sampleId( sampleEntity.getSampleId() );
        sampleGetRes.title( sampleEntity.getTitle() );
        sampleGetRes.text( sampleEntity.getText() );

        return sampleGetRes.build();
    }

    @Override
    public List<SampleGetRes> toSampleGetReses(List<Sample> sampleEntities) {
        if ( sampleEntities == null ) {
            return null;
        }

        List<SampleGetRes> list = new ArrayList<SampleGetRes>( sampleEntities.size() );
        for ( Sample sample : sampleEntities ) {
            list.add( toSampleGetRes( sample ) );
        }

        return list;
    }
}
