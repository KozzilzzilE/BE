package com.pocketco.domain.aiCodeReview.application;

import com.pocketco.domain.aiCodeReview.dto.AICodeReviewGetDTO;
import com.pocketco.domain.aiCodeReview.dto.AICodeReviewPostDTO;

public interface AICodeReviewService {
    AICodeReviewPostDTO createAICodeReview(Long userId, Long historyId);
    AICodeReviewGetDTO getAICodeReview(Long userId, Long historyId);
}