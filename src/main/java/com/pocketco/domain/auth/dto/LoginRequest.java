package com.pocketco.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginRequest(
        @Schema(description = "파이어베이스 인증 토큰", example = "eyJhbG...")
        String firebaseToken
) {}