package com.pcb.audy.domain.sample.controller;

import com.pcb.audy.domain.sample.dto.request.SampleSaveReq;
import com.pcb.audy.domain.sample.dto.response.SampleGetRes;
import com.pcb.audy.domain.sample.dto.response.SampleGetResList;
import com.pcb.audy.domain.sample.dto.response.SampleSaveRes;
import com.pcb.audy.domain.sample.service.SampleService;
import com.pcb.audy.global.response.result.BasicResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/sample")
@RequiredArgsConstructor
public class SampleController {

    private final SampleService sampleService;

    @PostMapping
    public BasicResponse<SampleSaveRes> saveSample(@RequestBody SampleSaveReq sampleSaveReq) {
        return BasicResponse.success(sampleService.saveSample(sampleSaveReq));
    }

    @GetMapping
    public BasicResponse<SampleGetResList> getAllSamples() {
        return BasicResponse.success(sampleService.getAllSamples());
    }

    @GetMapping("/{sampleId}")
    public BasicResponse<SampleGetRes> getSample(@PathVariable Long sampleId) {
        return BasicResponse.success(sampleService.getSample(sampleId));
    }
}
