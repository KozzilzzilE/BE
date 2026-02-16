package com.pocketco.domain.auth.dto;

import lombok.Builder;

@Builder // 💡 결과값 만들 때 편하도록 빌더만 씁니다!
public record SignupResponse(
        Long userId,
        String email,
        String nickname,
        String language
) {}