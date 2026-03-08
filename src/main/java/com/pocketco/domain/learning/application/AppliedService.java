package com.pocketco.domain.learning.application;

import com.pocketco.domain.admin.dto.AddAppliedRequest;
import com.pocketco.domain.admin.dto.AddAppliedResponse;
import com.pocketco.domain.learning.dto.LearningAppliedCompletionResponse;
import com.pocketco.domain.learning.dto.LearningAppliedExerciseResponse;
import com.pocketco.domain.learning.dto.LearningNotionCompletionResponse;

import java.util.List;

public interface AppliedService {
    List<AddAppliedResponse> addApplied(List<AddAppliedRequest> reqs);
    LearningAppliedExerciseResponse getLearningAppliedExercise(Long topicId, String language, Long userId);
    LearningAppliedCompletionResponse AppliedComplete(Long exerciseId, Long userId);
}