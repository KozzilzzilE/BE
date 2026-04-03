package com.pocketco.domain.judge0.application;

import com.pocketco.domain.judge0.dto.*;

import java.util.List;

public interface Judge0Service {
    List<Judge0LanguageResponse> getJudge0Languages();
    List<String> runCode(Long problemId, String language, CodeSubmitRequest request);
    SubmissionResponse submitCode(Long userId, Long problemId, String language, CodeSubmitRequest request);
    CodeRunResultResponse codeRunResult(String token);
    SubmissionResultResponse getSubmitResult(Long historyId, String submissionId);
    Judge0ResultResponse getJudge0ResultStatus(List<String> tokens);
}
