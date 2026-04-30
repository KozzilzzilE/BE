package com.pocketco.domain.aiCodeReview.api;

import com.pocketco.domain.aiCodeReview.application.AICodeReviewService;
import com.pocketco.domain.aiCodeReview.dto.AICodeReviewGetDTO;
import com.pocketco.domain.aiCodeReview.dto.AICodeReviewPostDTO;
import com.pocketco.global.common.code.status.SuccessStatus;
import com.pocketco.global.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/histories")
@RequiredArgsConstructor
public class AICodeReviewController {
    private final AICodeReviewService aiCodeReviewService;

    @PostMapping("/{historyId}/ai-review")
    public BaseResponse<AICodeReviewPostDTO> createAICodeReview(
            @AuthenticationPrincipal Long userId,
            @PathVariable(name = "historyId") Long historyId) {
        AICodeReviewPostDTO result = aiCodeReviewService.createAICodeReview(userId, historyId);
        return BaseResponse.onSuccess(SuccessStatus.AI_CODE_REVIEW_POST_SUCCESS, result);
    }

    @GetMapping("/{historyId}/ai-review")
    public BaseResponse<AICodeReviewGetDTO> getAICodeReview(
            @AuthenticationPrincipal Long userId,
            @PathVariable(name = "historyId") Long historyId) {
        AICodeReviewGetDTO result = aiCodeReviewService.getAICodeReview(userId, historyId);
        return BaseResponse.onSuccess(SuccessStatus.AI_CODE_REVIEW_GET_SUCCESS, result);
    }
}