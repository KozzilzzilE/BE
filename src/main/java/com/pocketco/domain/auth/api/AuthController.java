package com.pocketco.domain.auth.api;

import com.pocketco.domain.auth.application.AuthServiceImpl;
import com.pocketco.domain.auth.dto.LoginResponse;
import com.pocketco.domain.auth.dto.SignupRequest;
import com.pocketco.domain.auth.dto.SignupResponse;
import com.pocketco.global.common.code.status.SuccessStatus;
import com.pocketco.global.common.response.BaseResponse;
import com.pocketco.global.util.jwt.InMemoryTokenBlacklist;
import com.pocketco.global.util.jwt.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import com.pocketco.domain.auth.dto.LoginRequest;

@RestController
@RequestMapping("/api/v1/auths")
@RequiredArgsConstructor
public class AuthController {
    private final AuthServiceImpl authService;
    private final JwtTokenProvider jwt;
    private final InMemoryTokenBlacklist blacklist;

    @PostMapping("/signup")
    public BaseResponse<SignupResponse> signup(@RequestBody @Valid SignupRequest request) {
        SignupResponse result = authService.register(request);
        return BaseResponse.onSuccess(SuccessStatus.AUTH_REGISTER_SUCCESS, result);
    }

    @PostMapping("/login")
    public BaseResponse<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        LoginResponse result = authService.login(request.firebaseToken());
        return BaseResponse.onSuccess(SuccessStatus.AUTH_LOGIN_SUCCESS, result);
    }

    @PostMapping("/logout")
    public BaseResponse<Void> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        String token = authorization.substring(7);

        // 토큰 파싱 및 만료 시각 추출
        var exp = jwt.getExpiration(token);
        blacklist.add(token, exp);

        return BaseResponse.onSuccess(SuccessStatus.AUTH_LOGOUT_SUCCESS, null);
    }
}

