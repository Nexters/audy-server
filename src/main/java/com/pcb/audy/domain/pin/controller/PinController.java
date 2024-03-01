package com.pcb.audy.domain.pin.controller;

import com.pcb.audy.domain.pin.dto.request.PinDeleteReq;
import com.pcb.audy.domain.pin.dto.request.PinNameUpdateReq;
import com.pcb.audy.domain.pin.dto.request.PinOrderUpdateReq;
import com.pcb.audy.domain.pin.dto.request.PinSaveReq;
import com.pcb.audy.domain.pin.dto.response.PinDeleteRes;
import com.pcb.audy.domain.pin.dto.response.PinNameUpdateRes;
import com.pcb.audy.domain.pin.dto.response.PinOrderUpdateRes;
import com.pcb.audy.domain.pin.dto.response.PinSaveRes;
import com.pcb.audy.domain.pin.service.PinService;
import com.pcb.audy.global.response.BasicResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PinController {
    private final PinService pinService;

    @MessageMapping("/{courseId}/pin/addition")
    @SendTo("/sub/{courseId}/pin/addition")
    public BasicResponse<PinSaveRes> savePin(
            @DestinationVariable Long courseId, @RequestBody PinSaveReq pinSaveReq) {
        log.info("SavePinReq: " + pinSaveReq.toString());
        return BasicResponse.success(pinService.savePin(courseId, pinSaveReq));
    }

    @MessageMapping("/{courseId}/pin/modification/sequence")
    @SendTo("/sub/{courseId}/pin/modification/sequence")
    public BasicResponse<PinOrderUpdateRes> updateOrder(
            @DestinationVariable Long courseId, @RequestBody PinOrderUpdateReq pinOrderUpdateReq) {
        return BasicResponse.success(pinService.updatePinSequence(courseId, pinOrderUpdateReq));
    }

    @MessageMapping("/{courseId}/pin/modification/name")
    @SendTo("/sub/{courseId}/pin/modification/name")
    public BasicResponse<PinNameUpdateRes> updatePinName(
            @DestinationVariable Long courseId, @RequestBody PinNameUpdateReq pinNameUpdateReq) {
        return BasicResponse.success(pinService.updatePinName(courseId, pinNameUpdateReq));
    }

    @MessageMapping("/{courseId}/pin/removal")
    @SendTo("/sub/{courseId}/pin/removal")
    public BasicResponse<PinDeleteRes> deletePin(
            @DestinationVariable Long courseId, @RequestBody PinDeleteReq pinDeleteReq) {
        return BasicResponse.success(pinService.deletePin(courseId, pinDeleteReq));
    }
}
