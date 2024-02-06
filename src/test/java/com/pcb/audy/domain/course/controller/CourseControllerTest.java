package com.pcb.audy.domain.course.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pcb.audy.domain.BaseMvcTest;
import com.pcb.audy.domain.course.dto.request.CourseSaveReq;
import com.pcb.audy.domain.course.dto.request.CourseUpdateReq;
import com.pcb.audy.domain.course.dto.response.CourseSaveRes;
import com.pcb.audy.domain.course.dto.response.CourseUpdateRes;
import com.pcb.audy.domain.course.service.CourseService;
import com.pcb.audy.test.CourseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

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
    void courseName_수정() throws Exception {

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
}
