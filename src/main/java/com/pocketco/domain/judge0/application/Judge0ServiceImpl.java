package com.pocketco.domain.judge0.application;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pocketco.domain.aiCodeReview.entity.AICodeReviewStatus;
import com.pocketco.domain.judge0.converter.Judge0Converter;
import com.pocketco.domain.judge0.dto.Judge0LanguageExternal;
import com.pocketco.domain.judge0.dto.Judge0LanguageResponse;
import com.pocketco.domain.judge0.dto.CodeSubmitRequest;
import com.pocketco.domain.problem.entity.Problem;
import com.pocketco.domain.problem.entity.TestCase;
import com.pocketco.domain.problem.exception.ProblemNotFoundException;
import com.pocketco.domain.user.entity.*;
import com.pocketco.domain.user.exception.UserNotFoundException;
import com.pocketco.domain.user.exception.HistoryNotFoundException;
import com.pocketco.domain.user.repository.Judge0TokenRepository;
import com.pocketco.domain.user.repository.UserRepository;
import com.pocketco.global.util.judge0.Judge0Client;
import com.pocketco.global.util.judge0.Judge0SlotLimiter;
import com.pocketco.global.util.judge0.Judge0SubmitDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.pocketco.domain.user.repository.UserCodeRepository;
import org.springframework.core.io.buffer.DataBufferLimitException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.pocketco.domain.judge0.dto.SubmissionResultResponse;
import com.pocketco.domain.user.repository.HistoryRepository;
import com.pocketco.domain.judge0.dto.*;
import com.pocketco.domain.language.entity.Language;
import com.pocketco.domain.language.repository.LanguageRepository;
import com.pocketco.domain.problem.repository.ProblemRepository;
import com.pocketco.domain.problem.repository.TestCaseRepository;
import com.pocketco.domain.language.exception.LanguageNotFoundException;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;


import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class Judge0ServiceImpl implements Judge0Service {
    private final TestCaseRepository testCaseRepository;
    private final LanguageRepository languageRepository;
    private final ProblemRepository problemRepository;
    private final HistoryRepository historyRepository;
    private final Judge0TokenRepository judge0TokenRepository;
    private final UserRepository userRepository;
    private final Judge0SlotLimiter judge0SlotLimiter;
    private final Judge0Client judge0Client;
    private final Judge0SubmitDispatcher judge0SubmitDispatcher;
    private final UserCodeRepository userCodeRepository;


    @Override
    public List<Judge0LanguageResponse> getJudge0Languages() {
        return parseLanguages(judge0Client.getLanguagesJson());
    }

    @Override
    public List<String> runCode(Long problemId, String language, CodeSubmitRequest request) {
        // 1. 문제 존재 확인
        if (!problemRepository.existsById(problemId)) {
            throw new ProblemNotFoundException();
        }

        // 2. 언어 존재 확인
        int languageCode = languageRepository.findByName(language)
                .map(Language::getCode)
                .orElseThrow(LanguageNotFoundException::new);

        return judge0SlotLimiter.runSlot(() ->
                fetchRealTokensFromJudge0(problemId, languageCode, request, true));
    }


    @Override
    public SubmissionResponse submitCode(Long userId, Long problemId, String languageName, CodeSubmitRequest request) {
        User me = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Problem problem = problemRepository.findById(problemId).orElseThrow(ProblemNotFoundException::new);
        Language language = languageRepository.findByName(languageName).orElseThrow(LanguageNotFoundException::new);

        // 단 하나라도 정답을 맞춘 적이 있다면 isSolved == true 값 저장
        boolean isSolved = historyRepository.existsByUser_IdAndProblem_IdAndStatus(userId, problemId, HistoryStatus.ACCEPTED);

        // DB 저장
        History newHistory = History.builder()
                .sourceCode(request.sourceCode())
                .status(HistoryStatus.PROCESSING)
                .isSolved(isSolved)
                .aiStatus(AICodeReviewStatus.NOT_REQUESTED)
                .isSolutionViewed(false)
                .isGoalMet(false)
                .user(me)
                .language(language)
                .problem(problem)
                .build();

        History savedHistory = historyRepository.save(newHistory);

        List<TestCase> testCases = testCaseRepository.findByProblemId(problemId);

        for (TestCase tc : testCases) {
            Judge0Token judge0Token = Judge0Token.builder()
                    .token(null)
                    .statusId(0)
                    .history(savedHistory)
                    .testCase(tc)
                    .build();
            judge0TokenRepository.save(judge0Token);
        }

        userCodeRepository.deleteByUserAndProblem(me, problem);

        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() { judge0SubmitDispatcher.triggerAsync(); }
        });

        return SubmissionResponse.builder()
                .historyId(savedHistory.getId())
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
                    .time(0.0)
                    .build();
        }
        try {
            Judge0RunResultResponse response = judge0Client.getRunResult(token);

            if (response == null) {
                return CodeRunResultResponse.builder()
                        .statusId(-1)
                        .status("응답이 없습니다")
                        .time(0.0)
                        .build();
            }
            return Judge0Converter.toRunResultResponse(response);
        } catch (Exception e) {
            if (isDataBufferLimitException(e)) {
                return CodeRunResultResponse.builder()
                        .statusId(8)
                        .output("출력 크기가 너무 커서 결과를 표시할 수 없습니다.")
                        .status("Output Limit Exceeded")
                        .time(0.0)
                        .build();
            }
            log.error("Judge0 조회 실패. token={}", token, e);
            return CodeRunResultResponse.builder()
                    .statusId(-1)
                    .status("Judge0 조회 실패")
                    .time(0.0)
                    .build();
        }
    }

    @Override
    public SubmissionResultResponse getSubmitResult(Long historyId) {
        History history = historyRepository.findById(historyId).orElseThrow(HistoryNotFoundException::new);
        // 이미 채점이 완료되어 DB에 저장되어 있으면 채점이 완료 된 상태로 반환
        if (history.getStatus() != HistoryStatus.PROCESSING) {
            return SubmissionResultResponse.builder()
                    .success(true)
                    .status(history.getStatus())
                    .message("채점이 완료되었습니다.")
                    .progress(100)
                    .build();
        }

        List<Judge0Token> tokens = judge0TokenRepository.findByHistory_Id(historyId);
        if (tokens.isEmpty()) {
            return SubmissionResultResponse.builder()
                    .success(false)
                    .status(history.getStatus())
                    .message("채점 작업이 없습니다.")
                    .progress(0)
                    .build();
        }

        double progress = tokens.stream()
                .mapToDouble(t -> tokenProgress(t.getStatusId()))
                .average()
                .orElse(0.0);

        return SubmissionResultResponse.builder()
                .success(false)
                .status(HistoryStatus.PROCESSING)
                .message("채점중입니다...")
                .progress(progress)
                .build();
    }

    private double tokenProgress(int statusId) {
        if (statusId == 0) return 0.0;      // 생성
        if (statusId == 15) return 15.0;    // 제출 준비
        if (statusId == 1) return 25.0;     // Judge0 queue 여기서 오래 걸림
        if (statusId == 2) return 70.0;     // 실행
        if (statusId >= 3) return 100.0;    // 완료
        return 0.0;
    }

    private List<String> fetchRealTokensFromJudge0(Long problemId, int languageCode, CodeSubmitRequest request, boolean isSampleOnly) {
        List<TestCase> testCases;

        if (isSampleOnly) {
            testCases = testCaseRepository.findTop2ByProblemIdOrderByIdAsc(problemId);
        } else {
            testCases = testCaseRepository.findByProblemId(problemId);
        }
        return testCases.stream()
                .map(tc -> submitSingleTestCase(languageCode, request, tc))
                .toList();
    }

    private String submitSingleTestCase(int languageCode, CodeSubmitRequest request, TestCase tc) {
        Judge0IndividualRequest judge0Request = new Judge0IndividualRequest(
                request.sourceCode(), languageCode, request.timeLimit(), 256000, tc.getInput(), tc.getOutput());

        Judge0TokenResponse response = judge0Client.submit(judge0Request);

        return response.token();
    }

    @Override
    public Judge0ResultResponse getJudge0ResultStatus(List<String> tokens) {
        // DB 상태 업데이트를 위한 메서드

        HistoryStatus newStatus = HistoryStatus.PROCESSING;
        boolean allDone = true;

        for (String token : tokens) {
            try {
                Judge0StatusResponse response = judge0Client.getStatus(token);

                if (response == null || response.status() == null) {
                    allDone = false;
                    continue;
                }
                int statusId = response.status().id();

                if (statusId == 1 || statusId == 2) {
                    allDone = false;
                    continue;
                }
                newStatus = calcStatus(newStatus, statusId, response);
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

    private HistoryStatus calcStatus(HistoryStatus nowStatus, int statusId, Judge0StatusResponse response) {
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
                double ratio = response.time() / response.wallTime();
                if (ratio > 2.5){                // time과 wallTime의 비율이 이상하면 Judge0 시스템 오류로 판단
                    status = HistoryStatus.SYSTEM_ERROR;
                }
                else {
                    status = HistoryStatus.TIME_LIMIT_EXCEEDED;
                }
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

    private boolean isDataBufferLimitException(Throwable e) {
        while (e != null) {
            if (e instanceof DataBufferLimitException) {
                return true;
            }
            e = e.getCause();
        }
        return false;
    }
}