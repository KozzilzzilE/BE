package com.pocketco.domain.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddLanguageSettingRequest(
        @Schema(description = "설정한 언어 ID", example = "1")
        @NotNull
        Long languageId,

        @Schema(description = "설정한 언어의 해당 문제 시간 제약", example = "1000")
        @NotNull
        Integer timeLimitMs,

        @Schema(description = "설명한 언어의 모범 코드", example = "import java...")
        @NotBlank
        String code
) {}