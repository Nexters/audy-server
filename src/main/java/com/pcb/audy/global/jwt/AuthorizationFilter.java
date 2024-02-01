package com.pcb.audy.global.jwt;

import static com.pcb.audy.global.jwt.JwtUtils.ACCESS_TOKEN_HEADER;
import static com.pcb.audy.global.jwt.JwtUtils.REFRESH_TOKEN_HEADER;
import static com.pcb.audy.global.jwt.JwtUtils.TOKEN_TYPE;
import static com.pcb.audy.global.response.ResultCode.INVALID_TOKEN;
import static com.pcb.audy.global.response.ResultCode.NOT_FOUND_USER;

import com.pcb.audy.domain.user.entity.User;
import com.pcb.audy.domain.user.repository.UserRepository;
import com.pcb.audy.global.auth.PrincipalDetails;
import com.pcb.audy.global.exception.ExceptionHandler;
import com.pcb.audy.global.exception.GlobalException;
import com.pcb.audy.global.redis.RedisProvider;
import com.pcb.audy.global.validator.UserValidator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final RedisProvider redisProvider;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String accessHeader = request.getHeader(ACCESS_TOKEN_HEADER);
        String refreshHeader = request.getHeader(REFRESH_TOKEN_HEADER);
        if (!isExistHeader(accessHeader)) {
            ExceptionHandler.setErrorResponse(response, INVALID_TOKEN);
            return;
        }

        String accessToken = accessHeader.replace(TOKEN_TYPE, "");
        String email = jwtUtils.getEmail(accessToken);
        if (email == null) {
            if (!isExistHeader(refreshHeader)) {
                ExceptionHandler.setErrorResponse(response, INVALID_TOKEN);
                return;
            }

            String refreshToken = refreshHeader.replace(TOKEN_TYPE, "");
            email = getNewToken(response, refreshToken);
            if (email == null) {
                return;
            }
        }

        setAuthentication(email);
        chain.doFilter(request, response);
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
            ExceptionHandler.setErrorResponse(response, NOT_FOUND_USER);
            return null;
        }

        updateTokens(response, email);
        return email;
    }

    private void updateTokens(HttpServletResponse response, String email) {
        if (!redisProvider.hasKey(email)) {
            throw new GlobalException(INVALID_TOKEN);
        }

        jwtUtils.updateAccessToken(response, email);
        jwtUtils.updateRefreshToken(response, email);
    }

    private static boolean isExistHeader(String header) {
        return header != null && header.startsWith(TOKEN_TYPE);
    }
}
