package com.pcb.audy.domain.course.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pcb.audy.domain.BaseMvcTest;
import com.pcb.audy.domain.course.dto.request.CourseDeleteReq;
import com.pcb.audy.domain.course.dto.request.CourseSaveReq;
import com.pcb.audy.domain.course.dto.request.CourseUpdateReq;
import com.pcb.audy.domain.course.dto.response.*;
import com.pcb.audy.domain.course.service.CourseService;
import com.pcb.audy.test.CourseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.List;

@WebMvcTest(controllers = {CourseController.class})
class CourseControllerTest extends BaseMvcTest implements CourseTest {

    @MockBean private CourseService courseService;

    @Test
    @DisplayName("Course 저장 테스트")
    void course_저장() throws Exception {

        CourseSaveReq courseSaveReq = CourseSaveReq.builder().courseName(TEST_COURSE_NAME).build();
        CourseSaveRes courseSaveRes = CourseSaveRes.builder().courseId(TEST_COURSE_ID).build();
        when(courseService.saveCourse(any())).thenReturn(courseSaveRes);

        this.mockMvc
                .perform(
                        post("/v1/courses")
                                .content(objectMapper.writeValueAsString(courseSaveReq))
                                .contentType(MediaType.APPLICATION_JSON)
                                .principal(mockPrincipal))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("CourseName 수정 테스트")
    void course_이름_수정() throws Exception {

        CourseUpdateReq courseUpdateReq =
                CourseUpdateReq.builder().courseName(TEST_UPDATED_COURSE_NAME).build();

        CourseUpdateRes courseUpdateRes = new CourseUpdateRes();
        when(courseService.updateCourseName(any())).thenReturn(courseUpdateRes);

        this.mockMvc
                .perform(
                        patch("/v1/courses")
                                .content(objectMapper.writeValueAsString(courseUpdateReq))
                                .contentType(MediaType.APPLICATION_JSON)
                                .principal(mockPrincipal))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Course 삭제 테스트")
    void course_삭제() throws Exception {

        CourseDeleteReq courseDeleteReq = CourseDeleteReq.builder().courseId(TEST_COURSE_ID).build();

        CourseDeleteRes courseDeleteRes = new CourseDeleteRes();
        when(courseService.deleteCourse(any())).thenReturn(courseDeleteRes);

        this.mockMvc
                .perform(
                        delete("/v1/courses")
                                .content(objectMapper.writeValueAsString(courseDeleteReq))
                                .contentType(MediaType.APPLICATION_JSON)
                                .principal(mockPrincipal))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Course 전체 조회 테스트")
    void course_전체_조회() throws Exception {

        int testEditorCnt = 1;
        int testPinCnt = 1;

        CourseGetRes courseGetRes = CourseGetRes.builder()
                .courseId(TEST_COURSE_ID)
                .courseName(TEST_COURSE_NAME)
                .editorCnt(testEditorCnt)
                .pinCnt(testPinCnt)
                .build();
        CourseGetResList courseGetResList = CourseGetResList.builder()
                .courseGetResList(List.of(courseGetRes)).build();

        when(courseService.getAllCourse(any())).thenReturn(courseGetResList);

        this.mockMvc
                .perform(
                        get("/v1/courses/all")
                                .contentType(MediaType.APPLICATION_JSON)
                                .principal(mockPrincipal))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("내가 관리자인 Course 전체 조회 테스트")
    void 관리자_course_전체_조회() throws Exception {

        int testEditorCnt = 1;
        int testPinCnt = 1;

        CourseGetRes courseGetRes = CourseGetRes.builder()
                .courseId(TEST_COURSE_ID)
                .courseName(TEST_COURSE_NAME)
                .editorCnt(testEditorCnt)
                .pinCnt(testPinCnt)
                .build();
        CourseGetResList courseGetResList = CourseGetResList.builder()
                .courseGetResList(List.of(courseGetRes)).build();

        when(courseService.getOwnedCourse(any())).thenReturn(courseGetResList);

        this.mockMvc
                .perform(
                        get("/v1/courses/owner")
                                .contentType(MediaType.APPLICATION_JSON)
                                .principal(mockPrincipal))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("내가 멤버인 Course 전체 조회 테스트")
    void 멤버_course_전체_조회() throws Exception {

        int testEditorCnt = 1;
        int testPinCnt = 1;

        CourseGetRes courseGetRes = CourseGetRes.builder()
                .courseId(TEST_COURSE_ID)
                .courseName(TEST_COURSE_NAME)
                .editorCnt(testEditorCnt)
                .pinCnt(testPinCnt)
                .build();
        CourseGetResList courseGetResList = CourseGetResList.builder()
                .courseGetResList(List.of(courseGetRes)).build();

        when(courseService.getOwnedCourse(any())).thenReturn(courseGetResList);

        this.mockMvc
                .perform(
                        get("/v1/courses/member")
                                .contentType(MediaType.APPLICATION_JSON)
                                .principal(mockPrincipal))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
