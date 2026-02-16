package com.pocketco.domain.auth.dto;

import lombok.Builder;

@Builder
public record LoginResponse(
        String accessToken,
        String nickname,
        String language
) {}