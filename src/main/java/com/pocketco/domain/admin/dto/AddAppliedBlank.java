package com.pocketco.domain.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record AddAppliedBlank (
        @Schema(description = "빈칸 안의 내용", example = "HashMap")
        @NotBlank
        String content,

        @Schema(description = "정답인 빈칸 번호(오답인 빈칸이면 null)", example = "1")
        Integer answer
){}