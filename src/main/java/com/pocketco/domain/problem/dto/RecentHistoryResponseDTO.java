package com.pocketco.domain.problem.dto;

import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record RecentHistoryResponseDTO(
        Long historyId,
        Long problemId,
        String title,
        String sourceCode,
        String status,
        String language,
        LocalDateTime createdAt
) {}