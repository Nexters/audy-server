package com.pcb.audy.global.jwt;

import com.pcb.audy.global.jwt.tokens.AccessToken;
import com.pcb.audy.global.jwt.tokens.RefreshToken;
import com.pcb.audy.global.redis.RedisProvider;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtils {
    private final RedisProvider redisProvider;

    @Value("${jwt.secret}")
    private String jwtSecret;

    public static final String ACCESS_TOKEN_HEADER = "Authorization";
    public static final String REFRESH_TOKEN_HEADER = "RefreshToken";
    public static final String TOKEN_TYPE = "Bearer ";
    public static final String KEY_PREFIX = "jwt:";
    private final long ACCESS_TOKEN_EXPIRE_TIME = 10 * 60 * 1000L;
    private final long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L;
    private Key key;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public AccessToken getAccessToken(String email) {
        String accessToken =
                Jwts.builder()
                        .setSubject("accessToken")
                        .claim("email", email)
                        .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE_TIME))
                        .signWith(key, SignatureAlgorithm.HS256)
                        .compact();

        return AccessToken.builder().token(accessToken).expireTime(ACCESS_TOKEN_EXPIRE_TIME).build();
    }

    public RefreshToken getRefreshToken(String email) {
        String refreshToken =
                Jwts.builder()
                        .setSubject("refreshToken")
                        .claim("email", email)
                        .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRE_TIME))
                        .signWith(key, SignatureAlgorithm.HS256)
                        .compact();

        return RefreshToken.builder().token(refreshToken).expireTime(REFRESH_TOKEN_EXPIRE_TIME).build();
    }

    public String getEmail(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .get("email")
                    .toString();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public void updateAccessToken(HttpServletResponse response, String email) {
        AccessToken accessToken = getAccessToken(email);
        response.addHeader(ACCESS_TOKEN_HEADER, TOKEN_TYPE + accessToken.getToken());
    }

    public void updateRefreshToken(HttpServletResponse response, String email) {
        RefreshToken refreshToken = getRefreshToken(email);
        redisProvider.set(KEY_PREFIX + email, refreshToken.getToken(), refreshToken.getExpireTime());
        response.addHeader(REFRESH_TOKEN_HEADER, TOKEN_TYPE + refreshToken.getToken());
    }
}
