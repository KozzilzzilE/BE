package com.pocketco.domain.aiCodeReview.event;

import lombok.Builder;

@Builder
public record AICodeReviewRequestedEvent(
        Long historyId
) { }