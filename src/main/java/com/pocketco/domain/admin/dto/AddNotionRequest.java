package com.pocketco.domain.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record AddNotionRequest(
        @Schema(description = "알고리즘 번호", example = "1")
        @NotNull
        Long topicId,

        @Schema(description = "해당 알고리즘 개념 학습의 페이지 번호", example = "1")
        @NotNull
        Integer pageNo,

        @Schema(description = "학습 페이지의 제목", example = "해시 함수")
        @Size(max = 30)
        @NotBlank
        String title,

        @Schema(description = "포인트 개념", example = "해시 함수는 임의의 크기를 가진...")
        @Size(max = 150)
        @NotBlank
        String point,

        @Schema(description = "개념의 디테일한 설명", example = "문자열이나 숫자를 입력받아...")
        String detail,

        @Schema(description = "해당 페이지에 필요한 예제 코드들")
        @Valid
        @NotEmpty
        List<AddNotionCodeRequest> codes
) {}