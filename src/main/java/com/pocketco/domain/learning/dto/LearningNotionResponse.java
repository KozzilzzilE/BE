package com.pocketco.domain.learning.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record LearningNotionResponse(
        Long topicId,
        int count,
        List<LearningNotion> notions
) { }