package com.pocketco.domain.problem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

public class ProblemRequestDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TempStorageRequest {

        @NotBlank(message = "소스코드는 비어있을 수 없습니다.")
        private String sourceCode;
    }
}