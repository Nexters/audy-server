package com.pcb.audy.domain.pin.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pcb.audy.domain.BaseMvcTest;
import com.pcb.audy.domain.pin.dto.request.PinSaveReq;
import com.pcb.audy.domain.pin.dto.response.PinSaveRes;
import com.pcb.audy.domain.pin.service.PinService;
import com.pcb.audy.test.PinTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest(controllers = {PinController.class})
class PinControllerTest extends BaseMvcTest implements PinTest {
    @MockBean private PinService pinService;

    @Test
    @DisplayName("pin 저장 테스트")
    void pin_저장() throws Exception {
        PinSaveReq pinSaveReq =
                PinSaveReq.builder()
                        .courseId(TEST_COURSE_ID)
                        .pinName(TEST_PIN_NAME)
                        .originName(TEST_ORIGIN_NAME)
                        .latitude(TEST_LATITUDE)
                        .longitude(TEST_LONGITUDE)
                        .address(TEST_ADDRESS)
                        .sequence(TEST_SEQUENCE)
                        .build();
        PinSaveRes pinSaveRes = new PinSaveRes();
        when(pinService.savePin(any())).thenReturn(pinSaveRes);
        this.mockMvc
                .perform(
                        post("/v1/pins")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(pinSaveReq)))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
