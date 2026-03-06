package com.pocketco.domain.learning.dto;

import lombok.Builder;

@Builder
public record LearningNotionCode(
        String language,
        String content
) { }