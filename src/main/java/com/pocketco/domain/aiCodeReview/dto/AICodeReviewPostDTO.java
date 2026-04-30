package com.pocketco.domain.aiCodeReview.dto;

import com.pocketco.domain.aiCodeReview.entity.AICodeReviewStatus;
import lombok.Builder;

@Builder
public record AICodeReviewPostDTO(
        Long historyId,
        AICodeReviewStatus aiStatus
) { }