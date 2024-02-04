package com.pcb.audy.global.oauth.handler;

import static com.pcb.audy.global.jwt.JwtUtils.ACCESS_TOKEN_HEADER;
import static com.pcb.audy.global.jwt.JwtUtils.KEY_PREFIX;
import static com.pcb.audy.global.jwt.JwtUtils.REFRESH_TOKEN_HEADER;
import static com.pcb.audy.global.jwt.JwtUtils.TOKEN_TYPE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pcb.audy.global.auth.PrincipalDetails;
import com.pcb.audy.global.jwt.JwtUtils;
import com.pcb.audy.global.jwt.tokens.AccessToken;
import com.pcb.audy.global.jwt.tokens.RefreshToken;
import com.pcb.audy.global.oauth.service.OAuth2ServiceMapper;
import com.pcb.audy.global.redis.RedisProvider;
import com.pcb.audy.global.response.BasicResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtUtils jwtUtils;
    private final RedisProvider redisProvider;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        AccessToken accessToken = jwtUtils.getAccessToken(principalDetails.getUser().getEmail());
        RefreshToken refreshToken = jwtUtils.getRefreshToken(principalDetails.getUser().getEmail());
        registerTokens(response, principalDetails.getUser().getEmail(), accessToken, refreshToken);
        settingResponse(
                response,
                BasicResponse.success(
                        OAuth2ServiceMapper.INSTANCE.toOAuth2Res(principalDetails.getUser())));
    }

    private void registerTokens(
            HttpServletResponse response,
            String email,
            AccessToken accessToken,
            RefreshToken refreshToken) {
        response.addHeader(ACCESS_TOKEN_HEADER, TOKEN_TYPE + accessToken.getToken());
        response.addHeader(REFRESH_TOKEN_HEADER, TOKEN_TYPE + refreshToken.getToken());
        redisProvider.set(KEY_PREFIX + email, refreshToken.getToken(), refreshToken.getExpireTime());
    }

    private void settingResponse(HttpServletResponse response, BasicResponse<?> res)
            throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(objectMapper.writeValueAsString(res));
    }
}
