    package com.pocketco.domain.auth.api;

    import com.pocketco.domain.auth.application.AuthService;
    import com.pocketco.domain.auth.dto.LoginResponse;
    import com.pocketco.domain.auth.dto.SignupRequest;
    import com.pocketco.domain.auth.dto.SignupResponse;
    import com.pocketco.global.common.response.ApiResponse; // 👈 ApiResponse 위치!
    import lombok.RequiredArgsConstructor;
    import org.springframework.web.bind.annotation.*;
    import com.pocketco.domain.auth.dto.LoginRequest;

    import java.util.Map; // 👈 Map 에러 해결!

    @RestController
    @RequestMapping("/api/v1/auths")
    @RequiredArgsConstructor
    public class AuthController {
        private final AuthService authService;

        @PostMapping("/signup")
        public ApiResponse<SignupResponse> signup(@RequestBody SignupRequest request) {
            SignupResponse result = authService.register(request);
            // 💡 이제 명세서와 똑같은 구조로 데이터가 나갑니다!
            return ApiResponse.onSuccess("AUTH_200", "회원가입이 완료되었습니다.", result);
        }

        // ... 생략
        @PostMapping("/login")
        public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) { // 👈 여기를 수정!
            // request.firebaseToken() 으로 데이터를 꺼내 씁니다.
            LoginResponse result = authService.login(request.firebaseToken());
            return ApiResponse.onSuccess("AUTH_201", "로그인 성공", result);
        }
    }
