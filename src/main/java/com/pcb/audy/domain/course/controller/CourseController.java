package com.pcb.audy.domain.course.controller;

import com.pcb.audy.domain.course.dto.request.CourseDeleteReq;
import com.pcb.audy.domain.course.dto.request.CourseInviteReq;
import com.pcb.audy.domain.course.dto.request.CourseSaveReq;
import com.pcb.audy.domain.course.dto.request.CourseUpdateReq;
import com.pcb.audy.domain.course.dto.response.*;
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

    @PatchMapping
    public BasicResponse<CourseUpdateRes> updateCourse(
            @RequestBody CourseUpdateReq courseUpdateReq,
            @AuthenticationPrincipal PrincipalDetails userDetails) {
        courseUpdateReq.setUserId(userDetails.getUser().getUserId());
        return BasicResponse.success(courseService.updateCourseName(courseUpdateReq));
    }

    @DeleteMapping
    public BasicResponse<CourseDeleteRes> deleteCourse(
            @RequestBody CourseDeleteReq courseDeleteReq,
            @AuthenticationPrincipal PrincipalDetails userDetails) {
        courseDeleteReq.setUserId(userDetails.getUser().getUserId());
        return BasicResponse.success(courseService.deleteCourse(courseDeleteReq));
    }

    @PostMapping("/invite")
    public BasicResponse<CourseInviteRes> inviteCourse(
            @RequestBody CourseInviteReq courseInviteReq,
            @AuthenticationPrincipal PrincipalDetails userDetails) {
        courseInviteReq.setUserId(userDetails.getUser().getUserId());
        return BasicResponse.success(courseService.inviteCourse(courseInviteReq));
    }

    @GetMapping("/{courseId}")
    public BasicResponse<CourseDetailGetRes> getCourse(@PathVariable Long courseId) {
        return BasicResponse.success(courseService.getCourse(courseId));
    }

    @GetMapping("/all")
    public BasicResponse<CourseGetResList> getAllCourses(
            @AuthenticationPrincipal PrincipalDetails userDetails,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        return BasicResponse.success(
                courseService.getAllCourse(userDetails.getUser().getUserId(), page, limit));
    }

    @GetMapping("/owner")
    public BasicResponse<CourseGetResList> getOwnedCourses(
            @AuthenticationPrincipal PrincipalDetails userDetails,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        return BasicResponse.success(
                courseService.getOwnedCourse(userDetails.getUser().getUserId(), page, limit));
    }

    @GetMapping("/member")
    public BasicResponse<CourseGetResList> getMemberCourses(
            @AuthenticationPrincipal PrincipalDetails userDetails,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        return BasicResponse.success(
                courseService.getMemberCourse(userDetails.getUser().getUserId(), page, limit));
    }
}
