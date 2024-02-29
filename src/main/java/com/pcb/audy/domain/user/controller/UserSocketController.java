package com.pcb.audy.domain.user.controller;

import com.pcb.audy.domain.user.dto.response.UserGetResList;
import com.pcb.audy.domain.user.service.UserService;
import com.pcb.audy.global.response.BasicResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserSocketController {
    private final UserService userService;

    @MessageMapping("/{courseId}/user")
    @SendTo("/sub/{courseId}/user")
    public BasicResponse<UserGetResList> getCourseUser(@DestinationVariable Long courseId) {
        return BasicResponse.success(userService.getUsers(courseId));
    }
}
