package com.pocketco.domain.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record AddProblemRequest(
        @Schema(description = "알고리즘 번호", example = "1")
        @NotNull
        Long topicId,

        @Schema(description = "문제 제목", example = "배열에서 최대값 찾기")
        @NotBlank
        String title,

        @Schema(description = "난이도", example = "easy")
        @NotBlank
        String difficulty,

        @Schema(description = "난이도 정렬 순서", example = "1")
        @NotNull
        Integer difficultyOrder,

        @Schema(description = "문제 상세 설명", example = "정수 배열이...")
        @NotBlank
        String description,

        @Schema(description = "제약 조건", example = "1 ≤ n ≤ 10^6")
        @NotBlank
        String constraints,

        @Schema(description = "한 줄 해설", example = "2부터...")
        @NotBlank
        String lineSolution,

        @Schema(description = "모범 답안 설명", example = "모범 답안 설명")
        @NotBlank
        String solutionText,

        @Schema(description = "해당 문제의 테스크 케이스들")
        @Valid
        @NotEmpty
        List<AddTestCaseRequest> testCases,

        @Schema(description = "해당 문제의 언어별 모범 코드와 시간 제한")
        @Valid
        @NotEmpty
        List<AddLanguageSettingRequest> languageSettings
) {}