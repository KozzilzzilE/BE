package com.pocketco.domain.auth.application;

import com.pocketco.domain.auth.dto.LoginResponse;
import com.pocketco.domain.auth.dto.SignupRequest;
import com.pocketco.domain.auth.dto.SignupResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    public SignupResponse register(SignupRequest request) {
        // 실제 저장 로직이 들어갈 자리입니다.
        return SignupResponse.builder()
                .userId(5L)
                .email(request.email())
                .nickname(request.nickname())
                .language(request.language())
                .build();
    }

    public LoginResponse login(String token) {
        // 실제 로그인/토큰 검증 로직이 들어갈 자리입니다.
        return LoginResponse.builder()
                .accessToken("eyJhbG...")
                .nickname("하은최고")
                .language("JAVA")
                .build();
    }
}