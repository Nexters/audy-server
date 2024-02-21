package com.pcb.audy.domain.pin.controller;

import com.pcb.audy.domain.BaseMvcTest;
import com.pcb.audy.domain.pin.service.PinService;
import com.pcb.audy.test.PinTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(controllers = {PinController.class})
class PinControllerTest extends BaseMvcTest implements PinTest {
    @MockBean private PinService pinService;

    @Test
    @DisplayName("pin 저장 테스트")
    void pin_저장() {}

    @Test
    @DisplayName("pin 이름 수정 테스트")
    void pin_이름_수정() {}

    @Test
    @DisplayName("pin 삭제 테스트")
    void pin_삭제() {}
}
