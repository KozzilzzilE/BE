package com.pocketco.domain.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddTopicRequest (
        @Schema(description = "알고리즘명", example = "HASH")
        @NotBlank
        String name,

        @Schema(description = "화면에 보여질 이름", example = "해시")
        @NotNull
        String displayName
) {}