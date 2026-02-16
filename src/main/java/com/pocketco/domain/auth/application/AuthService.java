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
        return SignupResponse.builder()
                .userId(5L)
                .email(request.email())
                .nickname(request.nickname())
                .language(request.language())
                .build();
    }

    public LoginResponse login(String token) {
        return LoginResponse.builder()
                .accessToken("eyJhbG...")
                .nickname("하은최고")
                .language("JAVA")
                .build();
    }
}
