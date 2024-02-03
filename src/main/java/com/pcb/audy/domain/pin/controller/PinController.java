package com.pcb.audy.domain.pin.controller;

import com.pcb.audy.domain.pin.dto.request.PinSaveReq;
import com.pcb.audy.domain.pin.dto.response.PinSaveRes;
import com.pcb.audy.domain.pin.service.PinService;
import com.pcb.audy.global.response.BasicResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/pins")
@RequiredArgsConstructor
public class PinController {
    private final PinService pinService;

    @PostMapping
    public BasicResponse<PinSaveRes> savePin(@RequestBody PinSaveReq pinSaveReq) {
        return BasicResponse.success(pinService.savePin(pinSaveReq));
    }
}
