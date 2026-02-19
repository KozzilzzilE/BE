package com.pocketco.domain.auth.api;

import com.pocketco.domain.auth.application.AuthServiceImpl;
import com.pocketco.domain.auth.dto.LoginResponse;
import com.pocketco.domain.auth.dto.SignupRequest;
import com.pocketco.domain.auth.dto.SignupResponse;
import com.pocketco.global.common.code.status.SuccessStatus;
import com.pocketco.global.common.response.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.pocketco.domain.auth.dto.LoginRequest;

@RestController
@RequestMapping("/api/v1/auths")
@RequiredArgsConstructor
public class AuthController {
    private final AuthServiceImpl authService;

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
}

