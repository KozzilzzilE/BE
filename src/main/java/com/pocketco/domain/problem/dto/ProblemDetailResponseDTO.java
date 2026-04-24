package com.pocketco.domain.problem.dto;

import lombok.Builder;
import java.util.List;

@Builder
public record ProblemDetailResponseDTO(
        Long problemId,
        String title,
        String description,
        String constraint,
        List<TestCaseDTO> testCases,
        boolean isCompleted,
        Integer bookmarkCount,
        boolean isBookmark,
        Double timeLimit,
        Integer memoryLimit
) {}