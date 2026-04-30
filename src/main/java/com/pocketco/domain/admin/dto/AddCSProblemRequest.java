package com.pocketco.domain.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddCSProblemRequest(
    @Schema(description = "CS 문제 질문(최대 255byte)", example = "CS 문제 (최대 255 Byte)")
    @NotBlank
    String question,

    @Schema(description = "문제에 대한 True/False 정답", example = "true/false")
    @NotNull
    Boolean answer,

    @Schema(description = "문제에 대한 해셜", example = "문제 해설")
    @NotBlank
    String explanation
) { }