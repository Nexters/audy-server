package com.pcb.audy.domain.sample.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pcb.audy.domain.BaseMvcTest;
import com.pcb.audy.domain.sample.dto.request.SampleSaveReq;
import com.pcb.audy.domain.sample.dto.response.SampleGetRes;
import com.pcb.audy.domain.sample.dto.response.SampleGetResList;
import com.pcb.audy.domain.sample.dto.response.SampleSaveRes;
import com.pcb.audy.domain.sample.service.SampleService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest(controllers = {SampleController.class})
class SampleControllerTest extends BaseMvcTest {
    @MockBean private SampleService sampleService;

    @Test
    @DisplayName("샘플 저장 테스트")
    void 샘플_저장() throws Exception {
        Long sampleId = 1L;
        String title = "title";
        String text = "text";
        SampleSaveReq sampleSaveReq = SampleSaveReq.builder().title(title).text(text).build();
        SampleSaveRes sampleSaveRes = SampleSaveRes.builder().sampleId(sampleId).build();
        when(sampleService.saveSample(any())).thenReturn(sampleSaveRes);
        this.mockMvc
                .perform(
                        post("/v1/samples")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(sampleSaveReq)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("샘플 전체 조회 테스트")
    void 샘플_전체_조회() throws Exception {
        Long sampleId = 1L;
        String title = "title";
        String text = "text";
        SampleGetRes sampleGetRes =
                SampleGetRes.builder().sampleId(sampleId).title(title).text(text).build();
        SampleGetResList sampleGetResList =
                SampleGetResList.builder().sampleGetReses(List.of(sampleGetRes)).total(1).build();

        when(sampleService.getAllSamples()).thenReturn(sampleGetResList);
        this.mockMvc.perform(get("/v1/samples")).andDo(print()).andExpect(status().isOk());
    }
}
