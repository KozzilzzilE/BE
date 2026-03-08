package com.pocketco.domain.problem.dto;

import lombok.Builder;
import java.util.List;

@Builder
public record ProblemListResponseDTO(
        Long topicId,
        Integer count,
        List<ProblemResponseDTO> result
) {}