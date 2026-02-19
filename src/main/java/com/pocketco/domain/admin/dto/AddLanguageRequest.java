package com.pocketco.domain.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddLanguageRequest(
        @Schema(description = "언어명", example = "JAVA")
        @NotBlank
        String name,

        @Schema(description = "Judge0에서 인식하는 언어 코드", example = "32")
        @NotNull
        Integer code
) {}