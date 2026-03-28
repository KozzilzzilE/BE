package com.pocketco.domain.judge0.application;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pocketco.domain.judge0.dto.Judge0LanguageExternal;
import com.pocketco.domain.judge0.dto.Judge0LanguageResponse;
import com.pocketco.domain.judge0.dto.CodeSubmitRequest;
import com.pocketco.global.common.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Base64;
import java.nio.charset.StandardCharsets;
import com.pocketco.domain.judge0.dto.SubmissionResultResponse;
import reactor.core.publisher.Flux;
import java.util.UUID;
import com.pocketco.domain.judge0.dto.*;
import com.pocketco.domain.language.entity.Language;
import com.pocketco.domain.language.repository.LanguageRepository;
import com.pocketco.domain.problem.repository.ProblemRepository;
import com.pocketco.global.common.redis.RedisService;
import com.pocketco.global.common.code.status.ErrorStatus;
import com.pocketco.global.exception.handler.ProblemHandler;
import com.pocketco.global.exception.handler.LanguageHandler;


import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class Judge0ServiceImpl implements Judge0Service {
    private final WebClient webClient;
    private final RedisService redisService;
    private final LanguageRepository languageRepository;
    private final ProblemRepository problemRepository;

    @Override
    public List<Judge0LanguageResponse> getJudge0Languages() {
        String responseJson =  webClient.get()
                .uri("/languages")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return parseLanguages(responseJson);
    }

    @Override
    public List<String> runCode(Long problemId, String language, CodeSubmitRequest request) {
        // 1. 문제 존재 확인
        if (!problemRepository.existsById(problemId)) {
            throw new ProblemHandler(ErrorStatus.PROBLEM_NOT_FOUND);
        }

        // 2. 언어 존재 확인
        int languageId = languageRepository.findByName(language)
                .map(Language::getCode)
                .orElseThrow(() -> new LanguageHandler(ErrorStatus.LANGUAGE_NOT_FOUND));

        return fetchRealTokensFromJudge0(languageId, request);
    }

    @Override
    public String submitCode(Long problemId, String languageName, CodeSubmitRequest request) {
        if (!problemRepository.existsById(problemId)) {
            throw new ProblemHandler(ErrorStatus.PROBLEM_NOT_FOUND);
        }

        int languageId = languageRepository.findByName(languageName)
                .map(Language::getCode)
                .orElseThrow(() -> new LanguageHandler(ErrorStatus.LANGUAGE_NOT_FOUND));

        List<String> realTokens = fetchRealTokensFromJudge0(languageId, request);
        String submissionId = UUID.randomUUID().toString();
        redisService.saveTokens(submissionId, realTokens);

        return submissionId;
    }


    private List<Judge0LanguageResponse> parseLanguages(String responseJson) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {

            List<Judge0LanguageExternal> externalList =
                    objectMapper.readValue(responseJson, new TypeReference<List<Judge0LanguageExternal>>() { });

            List<Judge0LanguageResponse> result = externalList.stream()
                    .map(external -> mappingLanguageExternalToResponse(external))
                    .toList();
            return result;
        } catch (Exception e) {
            throw new RuntimeException("JSON 파싱 실패", e);
        }
    }

    private Judge0LanguageResponse mappingLanguageExternalToResponse(Judge0LanguageExternal external) {
        return Judge0LanguageResponse.builder()
                .code(external.id())
                .languageName(external.name())
                .build();
    }

    @Override
    public SubmissionResultResponse getResult(String submissionId) {
        List<String> tokens = redisService.getTokens(submissionId);

        for (String token : tokens) {
            Judge0StatusResponse response = webClient.get()
                    .uri("/submissions/" + token + "?base64_encoded=true&fields=status")
                    .retrieve()
                    .bodyToMono(Judge0StatusResponse.class)
                    .block();

            if (response != null) {
                int statusId = response.status().id();

                if (statusId == 1 || statusId == 2) {
                    return SubmissionResultResponse.builder()
                            .status("PROCESSING")
                            .message("채점중입니다")
                            .build();
                }
            }
        }

        redisService.deleteTokens(submissionId);

        return SubmissionResultResponse.builder()
                .status("DONE")
                .message("채점이 완료되었습니다.")
                .build();
    }



    private List<String> fetchRealTokensFromJudge0(int languageId, CodeSubmitRequest request) {
        String encodedSource = Base64.getEncoder()
                .encodeToString(request.sourceCode().getBytes(StandardCharsets.UTF_8));

        List<Judge0IndividualRequest> individualRequests = List.of(
                new Judge0IndividualRequest(encodedSource, languageId, "input1", "output1"),
                new Judge0IndividualRequest(encodedSource, languageId, "input2", "output2")
        );

        Judge0BatchRequest batchRequest = new Judge0BatchRequest(individualRequests);

        return webClient.post()
                .uri("/submissions/batch?base64_encoded=true&wait=false")
                .bodyValue(batchRequest)
                .retrieve()
                .bodyToFlux(Judge0TokenResponse.class)
                .collectList()
                .block()
                .stream()
                .map(Judge0TokenResponse::token)
                .toList();
    }
}
