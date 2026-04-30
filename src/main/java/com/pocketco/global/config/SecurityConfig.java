package com.pocketco.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pocketco.domain.user.entity.Role;
import com.pocketco.global.common.code.status.ErrorStatus;
import com.pocketco.global.common.response.BaseResponse;
import com.pocketco.global.util.jwt.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter; // 하은님이 만든 보안 요원
    private final CorsConfigurationSource corsConfigurationSource;
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 꺼두기
                .cors(cors -> cors.configurationSource(corsConfigurationSource)) // CORS 연결

                // 1. JWT를 사용하니까 서버가 세션을 기억하지 않게 설정 (매우 중요!)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 2. JWT 에러 예외처리 핸들러
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            String jwtException = (String) request.getAttribute("jwt_exception");

                            ErrorStatus errorStatus;

                            if ("TOKEN_EXPIRED".equals(jwtException)) {
                                errorStatus = ErrorStatus.EXPIRED_ACCESS_TOKEN;
                            } else if ("INVALID_TOKEN".equals(jwtException)) {
                                errorStatus = ErrorStatus.INVALID_ACCESS_TOKEN;
                            } else {
                                errorStatus = ErrorStatus.MISSING_ACCESS_TOKEN;
                            }

                            response.setStatus(errorStatus.getHttpStatus().value());
                            response.setContentType("application/json;charset=UTF-8");

                            BaseResponse<?> body =
                                    BaseResponse.onFailure(errorStatus, request.getRequestURI());

                            response.getWriter().write(objectMapper.writeValueAsString(body));
                        })

                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            ErrorStatus errorStatus = ErrorStatus._FORBIDDEN;

                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType("application/json;charset=UTF-8");

                            BaseResponse<?> body =
                                    BaseResponse.onFailure(errorStatus, request.getRequestURI());

                            response.getWriter().write(objectMapper.writeValueAsString(body));
                        })
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/admins/**").hasRole(Role.ADMIN.name())
                        // 3. 로그인, 회원가입, 스웨거는 하이패스!
                        .requestMatchers("/api/v1/auths/signup", "/api/v1/auths/login",
                                "/swagger-ui/**", "/v3/api-docs/**", "/actuator/health",
                                "/static/**", "/api/v1/languages/lists").permitAll()
                        // 4. 나머지는 무조건 '신분증(JWT)' 검사!
                        .anyRequest().authenticated()
                )

                // 🛡️ 5. 하은님이 만든 JWT 필터를 보안 검사기(UsernamePasswordAuthenticationFilter) 앞에 배치!
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }
}