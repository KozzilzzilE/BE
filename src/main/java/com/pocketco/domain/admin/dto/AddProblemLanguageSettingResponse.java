package com.pocketco.domain.admin.dto;

import lombok.Builder;

@Builder
public record AddProblemLanguageSettingResponse(
        String languageName,
        Long timeLimitId,
        Long solutionCodeId
) { }