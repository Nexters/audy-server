package com.pcb.audy.domain.pin.controller;

import com.pcb.audy.domain.pin.dto.request.PinDeleteReq;
import com.pcb.audy.domain.pin.dto.request.PinNameUpdateReq;
import com.pcb.audy.domain.pin.dto.request.PinSaveReq;
import com.pcb.audy.domain.pin.dto.response.PinDeleteRes;
import com.pcb.audy.domain.pin.dto.response.PinNameUpdateRes;
import com.pcb.audy.domain.pin.dto.response.PinSaveRes;
import com.pcb.audy.domain.pin.service.PinService;
import com.pcb.audy.global.response.BasicResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PinController {
    private final PinService pinService;

    @MessageMapping("/{courseId}/pin/addition")
    @SendTo("/sub/{courseId}/pin/addition")
    public BasicResponse<PinSaveRes> savePin(
            @DestinationVariable Long courseId, @RequestBody PinSaveReq pinSaveReq) {
        return BasicResponse.success(pinService.savePin(courseId, pinSaveReq));
    }

    @PatchMapping
    public BasicResponse<PinNameUpdateRes> updatePinName(
            @RequestBody PinNameUpdateReq pinNameUpdateReq) {
        return BasicResponse.success(pinService.updatePinName(pinNameUpdateReq));
    }

    @DeleteMapping
    public BasicResponse<PinDeleteRes> deletePin(@RequestBody PinDeleteReq pinDeleteReq) {
        return BasicResponse.success(pinService.deletePin(pinDeleteReq));
    }
}
