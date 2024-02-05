package com.pcb.audy.domain.course.controller;

import com.pcb.audy.domain.course.dto.request.CourseSaveReq;
import com.pcb.audy.domain.course.dto.response.CourseSaveRes;
import com.pcb.audy.domain.course.service.CourseService;
import com.pcb.audy.global.auth.PrincipalDetails;
import com.pcb.audy.global.response.BasicResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    public BasicResponse<CourseSaveRes> saveCourse(
            @RequestBody CourseSaveReq courseSaveReq,
            @AuthenticationPrincipal PrincipalDetails userDetails) {
        courseSaveReq.setUserId(userDetails.getUser().getUserId());
        return BasicResponse.success(courseService.saveCourse(courseSaveReq));
    }
}
