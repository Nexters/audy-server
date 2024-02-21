package com.pcb.audy.domain.editor.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static com.pcb.audy.global.meta.Role.MEMBER;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pcb.audy.domain.BaseMvcTest;
import com.pcb.audy.domain.editor.dto.request.EditorSaveReq;
import com.pcb.audy.domain.editor.dto.response.EditorSaveRes;
import com.pcb.audy.domain.editor.dto.request.EditorRoleUpdateReq;
import com.pcb.audy.domain.editor.dto.response.EditorRoleUpdateRes;
import com.pcb.audy.domain.editor.service.EditorService;
import com.pcb.audy.test.EditorTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest(controllers = {EditorController.class})
class EditorControllerTest extends BaseMvcTest implements EditorTest {
    @MockBean private EditorService editorService;

    @Test
    @DisplayName("editor 저장 테스트")
    void editor_저장() throws Exception {
        EditorSaveReq editorSaveReq =
                EditorSaveReq.builder().courseId(TEST_COURSE_ID).key(TEST_KEY).build();
        EditorSaveRes editorSaveRes = new EditorSaveRes();
        when(editorService.saveEditor(any())).thenReturn(editorSaveRes);
        this.mockMvc
                .perform(
                        post("/v1/editors")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(editorSaveReq))
                                .principal(this.mockPrincipal))
                .andDo(print())
                .andExpect(status().isOk());
    }
  
    @Test
    @DisplayName("editor 역할 수정 테스트")
    void editor_역할_수정() throws Exception {
        EditorRoleUpdateReq editorRoleUpdateReq =
                EditorRoleUpdateReq.builder()
                        .courseId(TEST_COURSE_ID)
                        .selectedUserId(TEST_ANOTHER_USER_ID)
                        .role(MEMBER)
                        .build();
        EditorRoleUpdateRes editorRoleUpdateRes = new EditorRoleUpdateRes();
        when(editorService.updateRoleEditor(any())).thenReturn(editorRoleUpdateRes);
        this.mockMvc
                .perform(
                        patch("/v1/editors")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(editorRoleUpdateReq))
                                .principal(this.mockPrincipal))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
