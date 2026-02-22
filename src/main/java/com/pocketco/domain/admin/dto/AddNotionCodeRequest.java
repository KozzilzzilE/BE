package com.pocketco.domain.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddNotionCodeRequest(
        @Schema(description = "언어 번호", example = "1")
        @NotNull
        Long languageId,

        @Schema(description = "코드", example = "₩₩₩java\nimport java.util.NoSuch...")
        @NotBlank
        String content
) {}