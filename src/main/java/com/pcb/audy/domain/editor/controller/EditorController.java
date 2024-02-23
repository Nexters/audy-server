package com.pcb.audy.domain.editor.controller;

import com.pcb.audy.domain.editor.dto.request.EditorDeleteReq;
import com.pcb.audy.domain.editor.dto.request.EditorRoleUpdateReq;
import com.pcb.audy.domain.editor.dto.request.EditorSaveReq;
import com.pcb.audy.domain.editor.dto.response.EditorDeleteRes;
import com.pcb.audy.domain.editor.dto.response.EditorRoleUpdateRes;
import com.pcb.audy.domain.editor.dto.response.EditorSaveRes;
import com.pcb.audy.domain.editor.service.EditorService;
import com.pcb.audy.global.auth.PrincipalDetails;
import com.pcb.audy.global.response.BasicResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/editors")
@RequiredArgsConstructor
public class EditorController {
    private final EditorService editorService;

    @PostMapping
    public BasicResponse<EditorSaveRes> saveEditor(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody EditorSaveReq editorSaveReq) {
        editorSaveReq.setUserId(principalDetails.getUser().getUserId());
        return BasicResponse.success(editorService.saveEditor(editorSaveReq));
    }

    @PatchMapping
    public BasicResponse<EditorRoleUpdateRes> updateEditorRole(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody EditorRoleUpdateReq editorRoleUpdateReq) {
        editorRoleUpdateReq.setUserId(principalDetails.getUser().getUserId());
        return BasicResponse.success(editorService.updateRoleEditor(editorRoleUpdateReq));
    }

    @DeleteMapping
    public BasicResponse<EditorDeleteRes> deleteEditor(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody EditorDeleteReq editorDeleteReq) {
        editorDeleteReq.setUserId(principalDetails.getUser().getUserId());
        return BasicResponse.success(editorService.deleteEditor(editorDeleteReq));
    }
}
