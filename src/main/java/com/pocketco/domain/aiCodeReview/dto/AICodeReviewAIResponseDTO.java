package com.pocketco.domain.aiCodeReview.dto;

import lombok.Builder;

@Builder
public record AICodeReviewAIResponseDTO(
        String aiReview,
        String aiImprovement,
        String aiCode
) { }