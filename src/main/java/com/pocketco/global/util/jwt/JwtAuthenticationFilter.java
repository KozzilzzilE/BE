package com.pocketco.global.util.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Authorization: Bearer <token>
 * 파싱해서 request attribute 로 userId 를 심는다.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwt;
    private final InMemoryTokenBlacklist blacklist;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        String auth = req.getHeader(HttpHeaders.AUTHORIZATION);

        if (auth == null || auth.isBlank()) {
            req.setAttribute("jwt_exception", "TOKEN_MISSING");
        } else if (!auth.startsWith("Bearer ")) {
            req.setAttribute("jwt_exception", "INVALID_TOKEN");
        }

        if (auth != null && auth.startsWith("Bearer ")) {
            String token = auth.substring(7).trim();
            if (token.isBlank()) {
                req.setAttribute("jwt_exception", "TOKEN_MISSING");
                SecurityContextHolder.clearContext();
            } else if (blacklist.isContain(token)) {
                // 블랙리스트 먼저 확인
                req.setAttribute("jwt_exception", "INVALID_TOKEN");
                SecurityContextHolder.clearContext();
            } else {
                try {
                    Jws<Claims> jws = jwt.parse(token);

                    Long userId = Long.valueOf(jws.getBody().getSubject());
                    String role = (String) jws.getBody().get("role");

                    // 1) 이후 컨트롤러에서 @RequestAttribute 쓰려면 계속 세팅
                    req.setAttribute("userId", userId);

                    // 2) 스프링 시큐리티가 인식할 인증객체 세팅 (여기가 핵심)
                    List<GrantedAuthority> authorities =
                            List.of(new SimpleGrantedAuthority("ROLE_" + role));

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userId, null, authorities);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));

                    SecurityContextHolder.getContext().setAuthentication(authentication);

                } catch (ExpiredJwtException e) {
                    req.setAttribute("jwt_exception", "TOKEN_EXPIRED");
                    SecurityContextHolder.clearContext(); // 토큰 문제면 인증 제거
                } catch (JwtException e) {
                    req.setAttribute("jwt_exception", "INVALID_TOKEN");
                    SecurityContextHolder.clearContext();
                }
            }
        }
        chain.doFilter(req, res);
    }
}