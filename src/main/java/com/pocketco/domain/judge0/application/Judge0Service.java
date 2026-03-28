package com.pocketco.domain.judge0.application;

import com.pocketco.domain.judge0.dto.Judge0LanguageResponse;
import com.pocketco.domain.judge0.dto.CodeSubmitRequest;
import com.pocketco.domain.judge0.dto.SubmissionResultResponse;

import java.util.List;

public interface Judge0Service {
    List<Judge0LanguageResponse> getJudge0Languages();
    List<String> runCode(Long problemId, String language, CodeSubmitRequest request);
    String submitCode(Long problemId, String language, CodeSubmitRequest request);
    SubmissionResultResponse getResult(String submissionId);

}
