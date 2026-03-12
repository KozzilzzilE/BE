package com.pocketco.domain.problem.dto;

import lombok.Builder;

@Builder
public record ProblemSolutionResponseDTO(
        String lineSolution,
        String solutionText,
        String language,
        String solutionCode
) {}