package com.pcb.audy.global.oauth.handler;

import com.pcb.audy.global.auth.PrincipalDetails;
import com.pcb.audy.global.jwt.JwtUtils;
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
        registerTokens(response, user.getUser().getEmail());
        return UriComponentsBuilder.fromUriString(getRedirectUri(request)).build().toString();
    }

    private void registerTokens(HttpServletResponse response, String email) {
        jwtUtils.setAccessToken(response, email);
        jwtUtils.setRefreshToken(response, email);
    }

    private String getRedirectUri(HttpServletRequest request) {
        if (request.getRequestURL().toString().startsWith(PREFIX_LOCAL_URL)) {
            return localRedirectUri;
        }
        return prodRedirectUri;
    }
}
