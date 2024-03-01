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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/pins")
@RequiredArgsConstructor
public class PinHttpController {
    private final PinService pinService;

    @PostMapping("/{courseId}")
    public BasicResponse<PinSaveRes> savePin(
            @PathVariable("courseId") Long courseId, @RequestBody PinSaveReq pinSaveReq) {
        return BasicResponse.success(pinService.savePin(courseId, pinSaveReq));
    }

    @PatchMapping("/{courseId}/order")
    public BasicResponse<PinOrderUpdateRes> updatePinOrder(
            @PathVariable("courseId") Long courseId, @RequestBody PinOrderUpdateReq pinOrderUpdateReq) {
        return BasicResponse.success(pinService.updatePinSequence(courseId, pinOrderUpdateReq));
    }

    @PatchMapping("/{courseId}/name")
    public BasicResponse<PinNameUpdateRes> updatePinName(
            @PathVariable("courseId") Long courseId, @RequestBody PinNameUpdateReq pinNameUpdateReq) {
        return BasicResponse.success(pinService.updatePinName(courseId, pinNameUpdateReq));
    }

    @DeleteMapping("/{courseId}")
    public BasicResponse<PinDeleteRes> deletePin(
            @PathVariable("courseId") Long courseId, @RequestBody PinDeleteReq pinDeleteReq) {
        return BasicResponse.success(pinService.deletePin(courseId, pinDeleteReq));
    }
}
