package com.pocketco.global.util.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.access-exp-min:120}")
    private long expMin;

    private SecretKey key; // 0.12.x에서는 SecretKey로 두는 게 편함

    @PostConstruct
    void init() {
        // HS256용 최소 256-bit(32바이트) 이상의 랜덤 시크릿이어야 함
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String createAccessToken(Long userId, String email, String role) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(expMin * 60);

        // 0.12.x 권장: subject()/issuedAt()/expiration()/claim()
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("email", email)
                .claim("role", role)
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                // 0.12.x: 알고리즘을 명시하려면 Jwts.SIG.HS256 사용
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public Jws<Claims> parse(String token) {
        // 0.12.x: parser() → verifyWith(key) → build() → parseSignedClaims(token)
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
    }

    public Long getUserId(String token) {
        return Long.valueOf(parse(token).getPayload().getSubject());
    }

    public Instant getExpiration(String token) {
        return parse(token).getPayload().getExpiration().toInstant();
    }
}