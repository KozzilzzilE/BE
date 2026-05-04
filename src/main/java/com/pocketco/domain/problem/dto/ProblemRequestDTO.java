package com.pocketco.domain.problem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ProblemRequestDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TempStorageRequest {
        private String sourceCode;
    }
}