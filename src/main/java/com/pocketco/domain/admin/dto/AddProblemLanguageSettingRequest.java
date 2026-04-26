package com.pocketco.domain.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddProblemLanguageSettingRequest(
        @Schema(description = "설정한 언어의 해당 문제 시간 제약", example = "1000")
        @NotNull
        Integer timeLimitMs,

        @Schema(description = "설명한 언어의 모범 코드", example = "import java...")
        @NotBlank
        String solutionCode
) { }