package com.pocketco.domain.judge0.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CodeSubmitRequest(
        @Schema(description = "사용자가 제출한 코드", example = "import ...")
        @NotBlank
        String sourceCode,

        @Schema(description = "시간 제한(초 단위)", example = "1.0")
        @NotNull
        Double timeLimit,

        @Schema(description = "문제별 메모리 제한(지금은 어떤 값을 줘도 256000으로 하드코딩)", example = "256000")
        @NotNull
        Integer memoryLimit
) { }