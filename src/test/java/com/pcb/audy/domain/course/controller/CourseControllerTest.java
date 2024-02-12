package com.pcb.audy.domain.course.controller;

import static com.pcb.audy.test.PinTest.*;
import static com.pcb.audy.test.UserTest.TEST_USER_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pcb.audy.domain.BaseMvcTest;
import com.pcb.audy.domain.course.dto.request.CourseDeleteReq;
import com.pcb.audy.domain.course.dto.request.CourseInviteReq;
import com.pcb.audy.domain.course.dto.request.CourseSaveReq;
import com.pcb.audy.domain.course.dto.request.CourseUpdateReq;
import com.pcb.audy.domain.course.dto.response.*;
import com.pcb.audy.domain.course.service.CourseService;
import com.pcb.audy.domain.pin.dto.response.PinGetRes;
import com.pcb.audy.test.CourseTest;
import java.util.List;
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
    @DisplayName("Course 상세 조회")
    void course_상세_조회() throws Exception {
        Long courseId = 1L;
        PinGetRes pinGetRes =
                PinGetRes.builder()
                        .pinId(TEST_PIN_ID)
                        .pinName(TEST_PIN_NAME)
                        .originName(TEST_ORIGIN_NAME)
                        .latitude(TEST_LATITUDE)
                        .longitude(TEST_LONGITUDE)
                        .address(TEST_ADDRESS)
                        .sequence(TEST_SEQUENCE)
                        .build();

        CourseDetailGetRes courseDetailGetRes =
                CourseDetailGetRes.builder()
                        .courseId(TEST_COURSE_ID)
                        .courseName(TEST_COURSE_NAME)
                        .pinList(List.of(pinGetRes))
                        .build();

        when(courseService.getCourse(any())).thenReturn(courseDetailGetRes);

        this.mockMvc
                .perform(
                        get("/v1/courses/{courseId}", courseId)
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
        int page = 0;
        int limit = 10;

        CourseGetRes courseGetRes =
                CourseGetRes.builder()
                        .courseId(TEST_COURSE_ID)
                        .courseName(TEST_COURSE_NAME)
                        .editorCnt(testEditorCnt)
                        .pinCnt(testPinCnt)
                        .build();
        CourseGetResList courseGetResList =
                CourseGetResList.builder().courseGetResList(List.of(courseGetRes)).build();

        when(courseService.getAllCourse(any(), anyInt(), anyInt())).thenReturn(courseGetResList);

        this.mockMvc
                .perform(
                        get("/v1/courses/all")
                                .param("page", String.valueOf(page))
                                .param("limit", String.valueOf(limit))
                                .contentType(MediaType.APPLICATION_JSON)
                                .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andDo(
                        document(
                                "courses/all", // API 문서의 이름 지정
                                queryParameters( // 요청 파라미터 문서화
                                        parameterWithName("page").description("불러올 페이지 number"),
                                        parameterWithName("limit").description("불러올 페이지 갯수"))));
    }

    @Test
    @DisplayName("내가 관리자인 Course 전체 조회 테스트")
    void 관리자_course_전체_조회() throws Exception {

        int testEditorCnt = 1;
        int testPinCnt = 1;
        int page = 0;
        int limit = 10;

        CourseGetRes courseGetRes =
                CourseGetRes.builder()
                        .courseId(TEST_COURSE_ID)
                        .courseName(TEST_COURSE_NAME)
                        .editorCnt(testEditorCnt)
                        .pinCnt(testPinCnt)
                        .build();
        CourseGetResList courseGetResList =
                CourseGetResList.builder().courseGetResList(List.of(courseGetRes)).build();

        when(courseService.getOwnedCourse(any(), anyInt(), anyInt())).thenReturn(courseGetResList);

        this.mockMvc
                .perform(
                        get("/v1/courses/owner")
                                .param("page", String.valueOf(page))
                                .param("limit", String.valueOf(limit))
                                .contentType(MediaType.APPLICATION_JSON)
                                .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andDo(
                        document(
                                "courses/owner", // API 문서의 이름 지정
                                queryParameters( // 요청 파라미터 문서화
                                        parameterWithName("page").description("불러올 페이지 number"),
                                        parameterWithName("limit").description("불러올 페이지 갯수"))));
    }

    @Test
    @DisplayName("내가 멤버인 Course 전체 조회 테스트")
    void 멤버_course_전체_조회() throws Exception {

        int testEditorCnt = 1;
        int testPinCnt = 1;
        int page = 0;
        int limit = 10;

        CourseGetRes courseGetRes =
                CourseGetRes.builder()
                        .courseId(TEST_COURSE_ID)
                        .courseName(TEST_COURSE_NAME)
                        .editorCnt(testEditorCnt)
                        .pinCnt(testPinCnt)
                        .build();
        CourseGetResList courseGetResList =
                CourseGetResList.builder().courseGetResList(List.of(courseGetRes)).build();

        when(courseService.getMemberCourse(any(), anyInt(), anyInt())).thenReturn(courseGetResList);

        this.mockMvc
                .perform(
                        get("/v1/courses/member")
                                .param("page", String.valueOf(page))
                                .param("limit", String.valueOf(limit))
                                .contentType(MediaType.APPLICATION_JSON)
                                .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andDo(
                        document(
                                "courses/owner", // API 문서의 이름 지정
                                queryParameters( // 요청 파라미터 문서화
                                        parameterWithName("page").description("불러올 페이지 number"),
                                        parameterWithName("limit").description("불러올 페이지 갯수"))));
    }

    @Test
    @DisplayName("초대 링크 생성 테스트")
    void 초대_링크_생성() throws Exception {

        CourseInviteReq courseInviteReq =
                CourseInviteReq.builder().courseId(TEST_COURSE_ID).userId(TEST_USER_ID).build();

        CourseInviteRes courseInviteRes = CourseInviteRes.builder().url(TEST_INVITE_URL).build();

        when(courseService.inviteCourse(any())).thenReturn(courseInviteRes);

        this.mockMvc
                .perform(
                        post("/v1/courses/invite")
                                .content(objectMapper.writeValueAsString(courseInviteReq))
                                .contentType(MediaType.APPLICATION_JSON)
                                .principal(mockPrincipal))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
