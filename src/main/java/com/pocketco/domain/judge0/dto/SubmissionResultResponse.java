package com.pocketco.domain.judge0.dto;

import lombok.Builder;
import java.util.List;

@Builder
public record SubmissionResultResponse(
        String status,
        String message
) { }