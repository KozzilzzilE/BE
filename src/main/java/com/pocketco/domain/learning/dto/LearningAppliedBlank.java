package com.pocketco.domain.learning.dto;

import lombok.Builder;

@Builder
public record LearningAppliedBlank(
        String content,
        Integer answer
) { }