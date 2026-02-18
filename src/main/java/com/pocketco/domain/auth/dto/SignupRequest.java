package com.pocketco.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record SignupRequest(
        @Schema(description = "파이어베이스 토큰", example = "eyJhbG...")
        String firebaseToken,

        @Schema(description = "이메일", example = "test2@test.com")
        String email,

        @Schema(description = "닉네임", example = "하은최고")
        String nickname,

        @Schema(description = "사용 언어", example = "JAVA")
        String language
) {}