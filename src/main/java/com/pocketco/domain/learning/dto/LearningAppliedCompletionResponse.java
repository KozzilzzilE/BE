package com.pocketco.domain.learning.dto;

import lombok.Builder;

@Builder
public record LearningAppliedCompletionResponse(
        Long exerciseId,
        String userName,
        boolean appliedCompleted
) { }