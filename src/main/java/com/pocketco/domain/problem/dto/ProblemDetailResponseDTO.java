package com.pocketco.domain.problem.dto;

import lombok.Builder;
import java.util.List;

@Builder
public record ProblemDetailResponseDTO(
        Long exerciseId,
        String title,
        String description,
        String constraint,
        List<TestCaseDTO> testCases,
        Boolean isCompleted
) {}