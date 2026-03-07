package com.pocketco.domain.topic.api;

import com.pocketco.domain.topic.application.TopicService;
import com.pocketco.domain.topic.dto.TopicListResponseDTO;
import com.pocketco.global.common.response.BaseResponse;
import com.pocketco.global.common.code.status.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Topic API", description = "알고리즘 주제 관련 API")
@RestController
@RequestMapping("/api/v1/topics")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;

    @Operation(summary = "알고리즘 주제 목록 조회", description = "학습할 수 있는 모든 알고리즘 주제 목록을 반환합니다.")
    @GetMapping("")
    public BaseResponse<TopicListResponseDTO> getTopics() {
        TopicListResponseDTO response = topicService.getTopicList();

        return BaseResponse.onSuccess(SuccessStatus.TOPIC_LIST_SUCCESS, response);
    }
}