package com.pocketco.domain.learning.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record LearningAppliedExerciseResponse(
        int count,
        List<LearningAppliedExercise> appliedExercises
) { }