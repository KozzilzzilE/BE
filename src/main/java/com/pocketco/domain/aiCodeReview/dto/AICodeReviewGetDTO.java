package com.pocketco.domain.aiCodeReview.dto;

import com.pocketco.domain.aiCodeReview.entity.AICodeReviewStatus;
import lombok.Builder;

@Builder
public record AICodeReviewGetDTO(
        Long historyId,
        AICodeReviewStatus aiStatus,
        String aiReview,
        String aiImprovement,
        String aiCode
) { }