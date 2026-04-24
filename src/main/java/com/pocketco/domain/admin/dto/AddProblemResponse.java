package com.pocketco.domain.admin.dto;

import lombok.Builder;

@Builder
public record AddProblemResponse(
        Long problemId,
        Long topicId,
        String title,
        String difficulty,
        Integer difficultyOrder,
        Integer testCaseCount,
        Integer languageSettingCount
) {}