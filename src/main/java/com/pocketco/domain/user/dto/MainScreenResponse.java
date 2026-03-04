package com.pocketco.domain.user.dto;

import lombok.Builder;

@Builder
public record MainScreenResponse(
        String nickname,
        Long languageId,
        String languageName
) { }