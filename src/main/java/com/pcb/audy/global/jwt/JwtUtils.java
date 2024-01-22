package com.pcb.audy.global.jwt;

import com.pcb.audy.global.jwt.tokens.AccessToken;
import com.pcb.audy.global.jwt.tokens.RefreshToken;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtUtils {
    @Value("${jwt.secret}")
    private String jwtSecret;
    private final String TOKEN_TYPE = "Bearer ";
    private final long ACCESS_TOKEN_EXPIRE_TIME = 10 * 60 * 1000L;
    private final long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L;
    private final Key key;

    public JwtUtils() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public AccessToken getAccessToken(String email) {
        String accessToken = Jwts.builder()
            .setSubject("accessToken")
            .claim("email", email)
            .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE_TIME))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();

        return AccessToken.builder()
            .token(accessToken)
            .expireTime(ACCESS_TOKEN_EXPIRE_TIME)
            .build();
    }

    public RefreshToken getRefreshToken(String email) {
        String refreshToken = Jwts.builder()
            .setSubject("refreshToken")
            .claim("email", email)
            .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRE_TIME))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();

        return RefreshToken.builder()
            .token(refreshToken)
            .expireTime(REFRESH_TOKEN_EXPIRE_TIME)
            .build();
    }

    public String getOauthId(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("email").toString();
    }

    public Date getExpiration(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getExpiration();
    }
}
