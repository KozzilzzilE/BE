package com.pocketco.domain.judge0.application;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pocketco.domain.judge0.converter.Judge0Converter;
import com.pocketco.domain.judge0.dto.Judge0LanguageExternal;
import com.pocketco.domain.judge0.dto.Judge0LanguageResponse;
import com.pocketco.domain.judge0.dto.CodeSubmitRequest;
import com.pocketco.domain.problem.entity.Problem;
import com.pocketco.domain.user.entity.*;
import com.pocketco.domain.user.exception.UserNotFoundException;
import com.pocketco.domain.user.repository.HistoryRepository;
import com.pocketco.domain.user.exception.HistoryNotFoundException;
import com.pocketco.domain.user.repository.Judge0TokenRepository;
import com.pocketco.domain.user.repository.UserRepository;
import com.pocketco.global.common.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import com.pocketco.domain.judge0.dto.SubmissionResultResponse;

import java.util.UUID;
import com.pocketco.domain.judge0.dto.*;
import com.pocketco.domain.language.entity.Language;
import com.pocketco.domain.language.repository.LanguageRepository;
import com.pocketco.domain.language.exception.LanguageNotFoundException; // 👈 추가!
import com.pocketco.domain.problem.repository.ProblemRepository;
import com.pocketco.global.common.code.status.ErrorStatus;
import com.pocketco.domain.problem.repository.TestCaseRepository;
import com.pocketco.domain.problem.exception.ProblemHandler;


import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class Judge0ServiceImpl implements Judge0Service {
    private final WebClient webClient;
    private final TestCaseRepository testCaseRepository;
    private final RedisService redisService;
    private final LanguageRepository languageRepository;
    private final ProblemRepository problemRepository;
    private final HistoryRepository historyRepository;
    private final Judge0TokenRepository judge0TokenRepository;
    private final UserRepository userRepository;

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
                .orElseThrow(LanguageNotFoundException::new);

        return fetchRealTokensFromJudge0(problemId, languageId, request, true);
    }


    @Override
    public SubmissionResponse submitCode(Long userId, Long problemId, String languageName, CodeSubmitRequest request) {
        User me = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Problem problem = problemRepository.findById(problemId).orElseThrow(() -> new ProblemHandler(ErrorStatus.PROBLEM_NOT_FOUND));
        Language language = languageRepository.findByName(languageName).orElseThrow(LanguageNotFoundException::new);
        int languageId = language.getCode();

        List<String> realTokens = fetchRealTokensFromJudge0(problemId, languageId, request, false);

        String submissionId = UUID.randomUUID().toString();
        redisService.saveTokens(submissionId, realTokens);

        // 단 하나라도 정답을 맞춘 적이 있다면 isSolved == true 값 저장
        boolean isSolved = historyRepository.existsByUser_IdAndProblem_IdAndStatus(userId, problemId, HistoryStatus.ACCEPTED);

        // DB 저장
        History newHistory = History.builder()
                .sourceCode(request.sourceCode())
                .status(HistoryStatus.PROCESSING)
                .isSolved(isSolved)
                .aiStatus(AIReviewStatus.NOT_REQUESTED)
                .isSolutionViewed(false)
                .isGoalMet(false)
                .user(me)
                .language(language)
                .problem(problem)
                .build();

        History savedHistory = historyRepository.save(newHistory);

        for (String token : realTokens) {
            Judge0Token judge0Token = Judge0Token.builder()
                    .token(token)
                    .statusId(1)
                    .history(savedHistory)
                    .build();
            judge0TokenRepository.save(judge0Token);
        }

        return SubmissionResponse.builder()
                .historyId(savedHistory.getId())
                .submissionId(submissionId)
                .build();
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
    public CodeRunResultResponse codeRunResult(String token) {
        if (token == null || token.isBlank()) {
            return CodeRunResultResponse.builder()
                    .statusId(-1)
                    .status("토큰이 없습니다")
                    .build();
        }
        try {
            Judge0RunResultResponse response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/submissions/{token}")
                            .queryParam("fields", "stdin,stdout,status")
                            .build(token))
                    .retrieve()
                    .bodyToMono(Judge0RunResultResponse.class)
                    .block();

            if (response == null) {
                return CodeRunResultResponse.builder()
                        .statusId(-1)
                        .status("응답이 없습니다")
                        .build();
            }
            return Judge0Converter.toRunResultResponse(response);
        } catch (Exception e) {
            return CodeRunResultResponse.builder()
                    .statusId(-1)
                    .status("Judge0 조회 실패")
                    .build();
        }
    }

    @Override
    public SubmissionResultResponse getSubmitResult(Long historyId, String submissionId) {
        History history = historyRepository.findById(historyId).orElseThrow(HistoryNotFoundException::new);
        // 이미 채점이 완료되어 DB에 저장되어 있으면 채점이 완료 된 상태로 반환
        if (history.getStatus() != HistoryStatus.PROCESSING) {
            return SubmissionResultResponse.builder()
                    .success(true)
                    .status(history.getStatus())
                    .message("채점이 이미 완료되었습니다.")
                    .build();
        }

        List<String> tokens = redisService.getTokens(submissionId);
        // redis에서 token을 못가져왔을 때
        if (tokens == null || tokens.isEmpty()) {
            return SubmissionResultResponse.builder()
                    .success(false)
                    .status(history.getStatus())
                    .message("submissionId로 토큰을 얻어오지 못했습니다.")
                    .build();
        }

        Judge0ResultResponse response = getJudge0ResultStatus(tokens);

        boolean allDone = response.allDone();
        HistoryStatus newStatus = response.newStatus();

        if (!allDone) {
            return SubmissionResultResponse.builder()
                    .success(false)
                    .status(HistoryStatus.PROCESSING)
                    .message("채점중입니다...")
                    .build();
        }

        int updated = historyRepository.updateStatus(historyId, newStatus);

        // 스케줄러에 의해 이미 업데이트 된 경우
        if (updated == 0) {
            newStatus = historyRepository.findById(historyId).orElseThrow(HistoryNotFoundException::new).getStatus();
        }
        if (updated == 1) {
            redisService.deleteTokens(submissionId);
        }

        return SubmissionResultResponse.builder()
                .success(true)
                .status(newStatus)
                .message("채점이 완료되었습니다.")
                .build();
    }

    private List<String> fetchRealTokensFromJudge0(Long problemId, int languageId, CodeSubmitRequest request, boolean isSampleOnly) {
        //String encodedSource = Base64.getEncoder()
                //.encodeToString(request.sourceCode().getBytes(StandardCharsets.UTF_8));

        List<com.pocketco.domain.problem.entity.TestCase> testCases;

        if (isSampleOnly) {
            testCases = testCaseRepository.findTop2ByProblemIdOrderByIdAsc(problemId);
        } else {
            testCases = testCaseRepository.findByProblemId(problemId);
        }
        List<Judge0IndividualRequest> individualRequests = testCases.stream()
                .map(tc -> new Judge0IndividualRequest(
                        request.sourceCode(),
                        languageId,
                        tc.getInput(),
                        tc.getOutput()
                ))
                .toList();

        Judge0BatchRequest batchRequest = new Judge0BatchRequest(individualRequests);
        // fetchRealTokensFromJudge0 로직 안에 추가
        System.out.println("--- [실행/제출] Judge0로 보내는 테스트케이스 ---");
        testCases.forEach(tc -> System.out.println("ID: " + tc.getId() + " | 입력: " + tc.getInput()));

        return webClient.post()
                .uri("/submissions/batch?wait=false")
                .bodyValue(batchRequest)
                .retrieve()
                .bodyToFlux(Judge0TokenResponse.class)
                .collectList()
                .block()
                .stream()
                .map(Judge0TokenResponse::token)
                .toList();
    }

    @Override
    public Judge0ResultResponse getJudge0ResultStatus(List<String> tokens) {
        // DB 상태 업데이트를 위한 메서드

        HistoryStatus newStatus = HistoryStatus.PROCESSING;
        boolean allDone = true;

        for (String token : tokens) {
            try {
                Judge0StatusResponse response = webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/submissions/{token}")
                                .queryParam("fields", "status")
                                .build(token))
                        .retrieve()
                        .bodyToMono(Judge0StatusResponse.class)
                        .block();

                if (response == null || response.status() == null) {
                    allDone = false;
                    continue;
                }
                int statusId = response.status().id();

                if (statusId == 1 || statusId == 2) {
                    allDone = false;
                    continue;
                }
                newStatus = calcStatus(newStatus, statusId);
                judge0TokenRepository.updateStatusWithToken(token, statusId);
            } catch (Exception e) {
                allDone = false;
            }
        }

        return Judge0ResultResponse.builder()
                .allDone(allDone)
                .newStatus(newStatus)
                .build();
    }

    private HistoryStatus calcStatus(HistoryStatus nowStatus, int statusId) {
        HistoryStatus status = nowStatus;

        // 상태 업데이트 우선 순위 => Compilation Error > System Error == Runtime Error > Time Limit Exceeded > Wrong Answer > Accepted
        if (statusId == 3 && nowStatus == HistoryStatus.PROCESSING) {
            status = HistoryStatus.ACCEPTED;
        }
        if (statusId == 4) {
            if (nowStatus == HistoryStatus.PROCESSING
                    || nowStatus== HistoryStatus.ACCEPTED) {
                status = HistoryStatus.WRONG_ANSWER;
            }
        }
        if (statusId == 5) {
            if (nowStatus != HistoryStatus.RUNTIME_ERROR
                    && nowStatus != HistoryStatus.COMPILATION_ERROR) {
                status = HistoryStatus.TIME_LIMIT_EXCEEDED;
            }
        }
        if (statusId == 6) {
            status = HistoryStatus.COMPILATION_ERROR;
        }
        if (statusId > 6) {
            if (nowStatus != HistoryStatus.COMPILATION_ERROR) {
                status = HistoryStatus.RUNTIME_ERROR;
            }
            if (statusId == 13) {
                status = HistoryStatus.SYSTEM_ERROR;
            }
        }

        return status;
    }
}
