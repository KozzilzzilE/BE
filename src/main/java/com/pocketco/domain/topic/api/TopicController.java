package com.pocketco.domain.topic.api;

import com.pocketco.domain.problem.application.ProblemService;
import com.pocketco.domain.problem.dto.ProblemListResponseDTO;
import com.pocketco.domain.topic.application.TopicService;
import com.pocketco.domain.topic.dto.TopicListResponseDTO;
import com.pocketco.global.common.response.BaseResponse;
import com.pocketco.global.common.code.status.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Topic API", description = "알고리즘 주제 관련 API")
@RestController
@RequestMapping("/api/v1/topics")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;
    private final ProblemService problemService;

    @GetMapping("")
    public BaseResponse<TopicListResponseDTO> getTopics() {
        TopicListResponseDTO response = topicService.getTopicList();

        return BaseResponse.onSuccess(SuccessStatus.TOPIC_LIST_SUCCESS, response);
    }

    @GetMapping("/{topicId}/problems") // 주소: /api/v1/topics/{topicId}/problems
    public BaseResponse<ProblemListResponseDTO> getProblemsByTopic(
            @AuthenticationPrincipal Long userId,
            @PathVariable(name = "topicId") Long topicId) {

        ProblemListResponseDTO response = problemService.getProblemListByTopic(topicId, userId);

        // 성공 응답 반환
        return BaseResponse.onSuccess(SuccessStatus.TOPIC_GET_PROBLEMS_SUCCESS, response);
    }
}