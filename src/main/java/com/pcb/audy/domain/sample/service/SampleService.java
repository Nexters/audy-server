package com.pcb.audy.domain.sample.service;

import com.pcb.audy.domain.sample.dto.request.SampleSaveReq;
import com.pcb.audy.domain.sample.dto.response.SampleGetRes;
import com.pcb.audy.domain.sample.dto.response.SampleGetResList;
import com.pcb.audy.domain.sample.dto.response.SampleSaveRes;
import com.pcb.audy.domain.sample.entity.Sample;
import com.pcb.audy.domain.sample.respository.SampleRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SampleService {

    private final SampleRepository sampleRepository;

    @Transactional
    public SampleSaveRes saveSample(SampleSaveReq sampleSaveReq) {
        return SampleServiceMapper.INSTANCE.toSampleSaveRes(
                sampleRepository.save(
                        Sample.builder()
                                .title(sampleSaveReq.getTitle())
                                .text(sampleSaveReq.getText())
                                .build()));
    }

    @Transactional(readOnly = true)
    public SampleGetResList getAllSamples() {
        List<SampleGetRes> sampleGetReses =
                SampleServiceMapper.INSTANCE.toSampleGetReses(sampleRepository.findAll());
        return SampleGetResList.builder()
                .sampleGetReses(sampleGetReses)
                .total(sampleGetReses.size())
                .build();
    }

    @Transactional(readOnly = true)
    public SampleGetRes getSample(Long sampleId) {
        Sample sample = sampleRepository.findBySampleId(sampleId);
        // SampleValidator.validate(sample);
        return SampleServiceMapper.INSTANCE.toSampleGetRes(sample);
    }
}
