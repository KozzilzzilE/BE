package com.pocketco.domain.judge0.dto;

import com.pocketco.domain.user.entity.HistoryStatus;
import lombok.Builder;
import java.util.List;

@Builder
public record SubmissionResultResponse(
        boolean success,
        HistoryStatus status,
        String message
) { }