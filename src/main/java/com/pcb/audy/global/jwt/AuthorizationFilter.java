package com.pcb.audy.global.jwt;

import static com.pcb.audy.global.jwt.JwtUtils.ACCESS_TOKEN_NAME;
import static com.pcb.audy.global.jwt.JwtUtils.REFRESH_TOKEN_NAME;
import static com.pcb.audy.global.jwt.JwtUtils.TOKEN_TYPE;
import static com.pcb.audy.global.response.ResultCode.INVALID_TOKEN;

import com.pcb.audy.domain.user.entity.User;
import com.pcb.audy.domain.user.repository.UserRepository;
import com.pcb.audy.global.auth.PrincipalDetails;
import com.pcb.audy.global.exception.GlobalException;
import com.pcb.audy.global.redis.RedisProvider;
import com.pcb.audy.global.validator.UserValidator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final RedisProvider redisProvider;
    private final UserRepository userRepository;

    private static final List<RequestMatcher> whiteList =
            List.of(
                    new AntPathRequestMatcher("/oauth2/**", HttpMethod.POST.name()),
                    new AntPathRequestMatcher("/course", HttpMethod.GET.name()));

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String accessHeader = getToken(request, ACCESS_TOKEN_NAME);
        String refreshHeader = getToken(request, REFRESH_TOKEN_NAME);

        log.info("accessHeader: " + accessHeader);
        log.info("refreshHeader: " + refreshHeader);
        if (!isExistHeader(accessHeader)) {
            throw new GlobalException(INVALID_TOKEN);
        }

        String accessToken = accessHeader.replace(TOKEN_TYPE, "");
        String email = jwtUtils.getEmail(accessToken);
        if (email == null) {
            if (!isExistHeader(refreshHeader)) {
                throw new GlobalException(INVALID_TOKEN);
            }

            String refreshToken = refreshHeader.replace(TOKEN_TYPE, "");
            email = getNewToken(response, refreshToken);
        }

        setAuthentication(email);
        chain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request, String name) {
        if (ArrayUtils.isEmpty(request.getCookies())) {
            return null;
        }

        Cookie findCookie =
                Arrays.stream(request.getCookies())
                        .filter(cookie -> name.equals(cookie.getName()))
                        .findFirst()
                        .orElse(null);
        if (findCookie == null) {
            return null;
        }
        return findCookie.getValue();
    }

    private void setAuthentication(String email) {
        User user = userRepository.findByEmail(email);
        UserValidator.validate(user);
        PrincipalDetails principalDetails = PrincipalDetails.builder().user(user).build();
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        principalDetails, null, principalDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String getNewToken(HttpServletResponse response, String refreshToken) {
        String email = jwtUtils.getEmail(refreshToken);
        if (email == null) {
            throw new GlobalException(INVALID_TOKEN);
        }

        updateTokens(response, email);
        return email;
    }

    private void updateTokens(HttpServletResponse response, String email) {
        if (!redisProvider.hasKey(email)) {
            throw new GlobalException(INVALID_TOKEN);
        }

        jwtUtils.setAccessToken(response, email);
        jwtUtils.setRefreshToken(response, email);
    }

    private static boolean isExistHeader(String header) {
        return header != null && header.startsWith(TOKEN_TYPE);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return whiteList.stream().anyMatch(url -> url.matches(request));
    }
}
