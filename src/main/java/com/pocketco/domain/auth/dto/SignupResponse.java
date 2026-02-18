package com.pocketco.domain.auth.dto;

import lombok.Builder;

@Builder
public record SignupResponse(
        Long userId,
        String email,
        String nickname,
        String language
) {}