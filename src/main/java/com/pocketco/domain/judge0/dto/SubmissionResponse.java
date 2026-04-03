package com.pocketco.domain.judge0.dto;

import lombok.Builder;

@Builder
public record SubmissionResponse(
        Long historyId,
        String submissionId
) { }