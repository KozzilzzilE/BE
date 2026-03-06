package com.pocketco.domain.learning.api;

import com.pocketco.domain.learning.application.AppliedService;
import com.pocketco.domain.learning.application.NotionService;
import com.pocketco.domain.learning.dto.LearningAppliedCompletionResponse;
import com.pocketco.domain.learning.dto.LearningAppliedExerciseResponse;
import com.pocketco.domain.learning.dto.LearningNotionCompletionResponse;
import com.pocketco.domain.learning.dto.LearningNotionResponse;
import com.pocketco.global.common.code.status.SuccessStatus;
import com.pocketco.global.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/learnings")
@RequiredArgsConstructor
public class LearningController {
    private final NotionService notionService;
    private final AppliedService appliedService;

    @GetMapping("/{topicId}/notions")
    public BaseResponse<LearningNotionResponse> getNotions(
            @PathVariable("topicId") Long topicId,
            @AuthenticationPrincipal Long userId,
            @RequestParam("language") String language) {
        LearningNotionResponse result = notionService.getLearningNotions(topicId, language, userId);
        return BaseResponse.onSuccess(SuccessStatus.LEARNING_NOTION_LIST_SUCCESS, result);
    }

    @PostMapping("/notions/completions/{notionId}")
    public BaseResponse<LearningNotionCompletionResponse> postNotionCompletions(
            @PathVariable("notionId") Long notionId,
            @AuthenticationPrincipal Long userId) {
        LearningNotionCompletionResponse result = notionService.notionComplete(notionId, userId);
        return BaseResponse.onSuccess(SuccessStatus.LEARNING_NOTION_COMPLETED_SUCCESS, result);
    }

    @GetMapping("/{topicId}/applications")
    public BaseResponse<LearningAppliedExerciseResponse> getApplications(
            @PathVariable("topicId") Long topicId,
            @AuthenticationPrincipal Long userId,
            @RequestParam("language") String language) {
        LearningAppliedExerciseResponse result = appliedService.getLearningAppliedExercise(topicId, language, userId);
        return BaseResponse.onSuccess(SuccessStatus.LEARNING_APPLIED_EXERCISE_LIST_SUCCESS, result);
    }

    @PostMapping("/applications/completions/{exerciseId}")
    public BaseResponse<LearningAppliedCompletionResponse> postAppliedCompletions(
            @PathVariable("exerciseId") Long exerciseId,
            @AuthenticationPrincipal Long userId) {
        LearningAppliedCompletionResponse result = appliedService.AppliedComplete(exerciseId, userId);
        return BaseResponse.onSuccess(SuccessStatus.LEARNING_APPLIED_EXERCISE_COMPLETED_SUCCESS, result);
    }
}