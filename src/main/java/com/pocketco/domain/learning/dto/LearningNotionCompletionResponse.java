package com.pocketco.domain.learning.dto;

import lombok.Builder;

@Builder
public record LearningNotionCompletionResponse(
        Long notionId,
        String userName,
        boolean notionCompleted
) { }