package com.pcb.audy.domain.editor.controller;

import com.pcb.audy.domain.editor.dto.request.EditorRoleUpdateReq;
import com.pcb.audy.domain.editor.dto.response.EditorRoleUpdateRes;
import com.pcb.audy.domain.editor.service.EditorService;
import com.pcb.audy.global.auth.PrincipalDetails;
import com.pcb.audy.global.response.BasicResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/editors")
@RequiredArgsConstructor
public class EditorController {
    private final EditorService editorService;

    @PatchMapping
    public BasicResponse<EditorRoleUpdateRes> updateEditorRole(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody EditorRoleUpdateReq editorRoleUpdateReq) {
        editorRoleUpdateReq.setUserId(principalDetails.getUser().getUserId());
        return BasicResponse.success(editorService.updateRoleEditor(editorRoleUpdateReq));
    }
}
