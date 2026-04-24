package com.pocketco.domain.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record AddTestCaseRequest(
        @Schema(description = "테스트 케이스 인풋 데이터", example = "5 3 7 2 9 5")
        @NotBlank
        String input,

        @Schema(description = "테스트 케이스 기대되는 아웃풋 데이터", example = "3")
        @NotBlank
        String output
) {}