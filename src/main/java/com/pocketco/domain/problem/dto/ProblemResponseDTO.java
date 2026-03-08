package com.pocketco.domain.problem.dto;

import lombok.Builder;

@Builder // ✨ 이게 있어야 .builder()를 쓸 수 있어요!
public record ProblemResponseDTO(
        Long problemId,
        String title,
        String difficulty,
        String difficultyDisplayName
) {}