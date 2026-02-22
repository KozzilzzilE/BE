package com.pocketco.domain.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record AddAppliedRequest(
        @Schema(description = "알고리즘 번호", example = "1")
        @NotNull
        Long topicId,

        @Schema(description = "해당 알고리즘 응용 학습의 페이지 번호", example = "1")
        @NotNull
        Integer orderNo,

        @Schema(description = "응용 학습 페이지의 제목", example = "해시맵으로 문자 개수 세기")
        @NotBlank
        String title,

        @Schema(description = "응용 학습 문제 설명", example = "문자열에서 각 문자의 개수를 세는 코드의 빈칸에 알맞은 정답을 선택하세요")
        @NotBlank
        String description,

        @Schema(description = "해당 페이지에 필요한 예제 코드들")
        @Valid
        @NotEmpty
        List<AddAppliedCodeRequest> codes
) { }