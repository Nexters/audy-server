package com.pcb.audy.global.oauth.handler;

import static com.pcb.audy.global.jwt.JwtUtils.ACCESS_TOKEN_HEADER;
import static com.pcb.audy.global.jwt.JwtUtils.KEY_PREFIX;
import static com.pcb.audy.global.jwt.JwtUtils.REFRESH_TOKEN_HEADER;

import com.pcb.audy.global.auth.PrincipalDetails;
import com.pcb.audy.global.jwt.JwtUtils;
import com.pcb.audy.global.jwt.tokens.AccessToken;
import com.pcb.audy.global.jwt.tokens.RefreshToken;
import com.pcb.audy.global.redis.RedisProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtUtils jwtUtils;
    private final RedisProvider redisProvider;

    private final String IS_COMMITTED = "Response has already been committed. Unable to redirect to ";
    private final String PREFIX_LOCAL_URL = "http://localhost:8080";

    @Value("${local-redirect-uri}")
    private String localRedirectUri;

    @Value("${prod-redirect-uri}")
    private String prodRedirectUri;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            logger.debug(IS_COMMITTED + targetUrl);
            return;
        }

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    @Override
    protected String determineTargetUrl(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        PrincipalDetails user = (PrincipalDetails) authentication.getPrincipal();

        AccessToken accessToken = jwtUtils.getAccessToken(user.getUser().getEmail());
        RefreshToken refreshToken = jwtUtils.getRefreshToken(user.getUser().getEmail());
        registerTokens(response, user.getUser().getEmail(), accessToken, refreshToken);
        return UriComponentsBuilder.fromUriString(getRedirectUri(request)).build().toString();
    }

    private void registerTokens(
            HttpServletResponse response,
            String email,
            AccessToken accessToken,
            RefreshToken refreshToken) {
        setCookie(response, ACCESS_TOKEN_HEADER, accessToken.getToken(), accessToken.getExpireTime());
        setCookie(
                response, REFRESH_TOKEN_HEADER, refreshToken.getToken(), refreshToken.getExpireTime());
        redisProvider.set(KEY_PREFIX + email, refreshToken.getToken(), refreshToken.getExpireTime());
    }

    private void setCookie(HttpServletResponse response, String key, String token, Long expireTime) {
        Cookie cookie = new Cookie(key, token);
        cookie.setMaxAge(Math.toIntExact(expireTime));
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    private String getRedirectUri(HttpServletRequest request) {
        if (request.getRequestURL().toString().startsWith(PREFIX_LOCAL_URL)) {
            return localRedirectUri;
        }
        return prodRedirectUri;
    }
}
