package com.pocketco.domain.judge0.converter;


import com.pocketco.domain.judge0.dto.CodeRunResultResponse;
import com.pocketco.domain.judge0.dto.Judge0RunResultResponse;

public class Judge0Converter {
    public static CodeRunResultResponse toRunResultResponse(Judge0RunResultResponse judge0) {
        return CodeRunResultResponse.builder()
                .statusId(judge0.status().id())
                .status(judge0.status().description())
                .input(judge0.stdin())
                .output(judge0.stdout())
                .build();
    }
}