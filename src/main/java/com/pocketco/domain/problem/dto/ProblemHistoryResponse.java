package com.pocketco.domain.problem.dto;

import com.pocketco.domain.user.entity.History;
import com.pocketco.domain.user.entity.HistoryStatus;
import com.pocketco.global.util.time.TimeUtils;

import java.time.LocalDateTime;

public record ProblemHistoryResponse(
        Long historyId,
        String sourceCode,
        HistoryStatus status,
        String language,
        LocalDateTime createdAt
) {
    public static ProblemHistoryResponse from(History h) {
        return new ProblemHistoryResponse(
                h.getId(),
                h.getSourceCode(),
                h.getStatus(),
                h.getLanguage().getName(),
                TimeUtils.toKst(h.getCreatedAt())
        );
    }
}