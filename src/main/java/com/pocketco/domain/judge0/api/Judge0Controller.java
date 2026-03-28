package com.pocketco.domain.judge0.api;

import com.pocketco.domain.judge0.application.Judge0Service;
import com.pocketco.domain.judge0.dto.CodeSubmitRequest;
import com.pocketco.global.common.code.status.SuccessStatus;
import com.pocketco.global.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/problems")
public class Judge0Controller {

    private final Judge0Service judge0Service;

    @PostMapping("/{problemId}/runs")
    public BaseResponse<List<Map<String, String>>> runCode(
            @PathVariable Long problemId,
            @RequestParam String language,
            @RequestBody CodeSubmitRequest request) {

        List<String> tokens = judge0Service.runCode(problemId, language, request);

        List<Map<String, String>> result = tokens.stream()
                .map(t -> Map.of("token", t))
                .collect(Collectors.toList());

        return BaseResponse.onSuccess(SuccessStatus.PROBLEM_RUN_SUCCESS, result);
    }

    @PostMapping("/{problemId}/submissions")
    public BaseResponse<Map<String, String>> submitCode(
            @PathVariable Long problemId,
            @RequestParam String language,
            @RequestBody CodeSubmitRequest request) {

        String submissionId = judge0Service.submitCode(problemId, language, request);

        return BaseResponse.onSuccess(SuccessStatus.PROBLEM_SUBMIT_SUCCESS, Map.of("submissionId", submissionId));
    }
}