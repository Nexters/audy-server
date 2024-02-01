package com.pcb.audy.domain.user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pcb.audy.domain.BaseMvcTest;
import com.pcb.audy.domain.user.dto.response.UserGetRes;
import com.pcb.audy.domain.user.service.UserService;
import com.pcb.audy.test.UserTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(controllers = {UserController.class})
class UserControllerTest extends BaseMvcTest implements UserTest {
    @MockBean private UserService userService;

    @Test
    @DisplayName("유저 조회 테스트")
    void 유저_조회() throws Exception {
        Long userId = TEST_USER_ID;
        UserGetRes userGetRes =
                UserGetRes.builder()
                        .userId(TEST_USER_ID)
                        .email(TEST_USER_EMAIL)
                        .username(TEST_USER_NAME)
                        .imageUrl(TEST_USER_IMAGE_URL)
                        .build();
        when(userService.getUser(any())).thenReturn(userGetRes);
        this.mockMvc
                .perform(get("/v1/users").param("userId", String.valueOf(userId)))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
