package com.pocketco.domain.problem.dto;

import lombok.Builder;
import java.util.List;

public class ProblemAllResponseDTO {

    @Builder
    public record ProblemListResponse(
            List<ProblemItemDTO> problemList,
            Integer page,
            Integer size,
            Integer totalPage,
            Long totalElements
    ) {}

    @Builder
    public record ProblemItemDTO(
            Long problemId,
            String title,
            String difficulty,
            String difficultyDisplayName,
            Long bookmarkCount,
            boolean isBookmark,
            boolean isCompleted,
            String topicName,
            String topicDisplayName
    ) {}
}