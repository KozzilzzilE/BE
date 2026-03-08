package com.pocketco.domain.learning.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record LearningAppliedExercise(
        Long exerciseId,
        String title,
        String description,
        String codeTemplate,
        boolean appliedCompleted,
        int totalBlanks,
        List<LearningAppliedBlank> blanks
) { }