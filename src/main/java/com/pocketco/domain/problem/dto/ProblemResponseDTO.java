package com.pocketco.domain.problem.dto;

import lombok.Builder;

@Builder
public record ProblemResponseDTO(
        Long problemId,
        String title,
        String difficulty,
        String difficultyDisplayName
) {}