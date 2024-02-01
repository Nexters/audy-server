package com.pcb.audy.domain.user.controller;

import com.pcb.audy.domain.user.dto.response.UserGetRes;
import com.pcb.audy.domain.user.service.UserService;
import com.pcb.audy.global.response.BasicResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public BasicResponse<UserGetRes> getUser(@RequestParam Long userId) {
        return BasicResponse.success(userService.getUser(userId));
    }
}
