package com.pocketco.domain.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record AddAppliedCodeRequest (
        @Schema(description = "언어 번호", example = "1")
        @NotNull
        Long languageId,

        @Schema(description = "빈칸이 포함된 코드", example = "₩₩₩java\nimport java.util.NoSuch...")
        @NotBlank
        String codeTemplate,

        @Schema(description = "해당 페이지의 빈칸들")
        @Valid
        @NotEmpty
        List<AddAppliedBlank> blanks
){}