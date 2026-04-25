package com.pocketco.domain.judge0.api;

import com.pocketco.domain.judge0.application.Judge0Service;
import com.pocketco.domain.judge0.dto.CodeRunResultResponse;
import com.pocketco.domain.judge0.dto.CodeSubmitRequest;
import com.pocketco.domain.judge0.dto.SubmissionResponse;
import com.pocketco.domain.judge0.dto.SubmissionResultResponse;
import com.pocketco.global.common.code.status.SuccessStatus;
import com.pocketco.global.common.response.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            @RequestBody @Valid CodeSubmitRequest request) {

        List<String> tokens = judge0Service.runCode(problemId, language, request);

        List<Map<String, String>> result = tokens.stream()
                .map(t -> Map.of("token", t))
                .collect(Collectors.toList());

        return BaseResponse.onSuccess(SuccessStatus.PROBLEM_RUN_SUCCESS, result);
    }

    @PostMapping("/{problemId}/submissions")
    public BaseResponse<SubmissionResponse> submitCode(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long problemId,
            @RequestParam String language,
            @RequestBody @Valid CodeSubmitRequest request) {

        SubmissionResponse result = judge0Service.submitCode(userId, problemId, language, request);

        return BaseResponse.onSuccess(SuccessStatus.PROBLEM_SUBMIT_SUCCESS, result);
    }

    @GetMapping("/runs/{token}/results")
    public BaseResponse<CodeRunResultResponse> runResults(@PathVariable("token") String token) {
        CodeRunResultResponse result = judge0Service.codeRunResult(token);
        return BaseResponse.onSuccess(SuccessStatus.PROBLEM_RUN_RESULT_SUCCESS, result);
    }

    @GetMapping("/submissions/{historyId}/results")
    public BaseResponse<SubmissionResultResponse> submitResult(
            @PathVariable("historyId") Long historyId) {
        SubmissionResultResponse result = judge0Service.getSubmitResult(historyId);
        return BaseResponse.onSuccess(SuccessStatus.PROBLEM_SUBMIT_RESULT_SUCCESS, result);
    }
}