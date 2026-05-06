package com.pocketco.domain.problem.application;

import com.pocketco.domain.admin.dto.*;
import com.pocketco.domain.language.application.LanguageService;
import com.pocketco.domain.language.entity.Language;
import com.pocketco.domain.problem.dto.ProblemListResponseDTO;
import com.pocketco.domain.problem.dto.ProblemResponseDTO;
import com.pocketco.domain.problem.dto.*;
import com.pocketco.domain.problem.entity.*;
import com.pocketco.domain.problem.exception.ProblemAlreadyExistsException;
import com.pocketco.domain.problem.exception.ProblemNotFoundException;
import com.pocketco.domain.problem.exception.ProblemSolutionNotFoundException;
import com.pocketco.domain.problem.exception.ProblemLanguageSolutionCodeAlreadyExistsException;
import com.pocketco.domain.problem.exception.ProblemLanguageTimeLimitAlreadyExistsException;
import com.pocketco.domain.problem.repository.*;
import com.pocketco.domain.topic.entity.Topic;
import com.pocketco.domain.topic.exception.TopicNotFoundException;
import com.pocketco.domain.topic.repository.TopicRepository;
import com.pocketco.domain.user.entity.HistoryStatus;
import com.pocketco.domain.user.exception.UserNotFoundException;
import com.pocketco.domain.user.repository.BookmarkProblemRepository;
import com.pocketco.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.pocketco.domain.problem.exception.ProblemInvalidDifficultyException;
import com.pocketco.domain.problem.dto.TempStorageResponseDTO;
import com.pocketco.domain.problem.dto.ProblemRequestDTO;
import com.pocketco.domain.problem.entity.UserProblemCode;
import com.pocketco.domain.problem.repository.UserProblemCodeRepository;
import com.pocketco.domain.user.entity.User;
import com.pocketco.domain.user.repository.HistoryRepository;
import com.pocketco.domain.user.entity.History;
import java.time.ZoneId;
import java.time.LocalDateTime;


import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional

public class ProblemServiceImpl implements ProblemService {
    private final ProblemRepository problemRepository;
    private final TestCaseRepository testCaseRepository;
    private final SolutionCodeRepository solutionCodeRepository;
    private final TopicRepository topicRepository;
    private final LanguageService languageService;
    private final HistoryRepository historyRepository;
    private final UserRepository userRepository;
    private final TimeLimitRepository timeLimitRepository;
    private final BookmarkProblemRepository bookmarkRepository;
    private final UserProblemCodeRepository userProblemCodeRepository;

    @Override
    public List<AddProblemResponse> addProblems(AddProblemRequests reqs) {
        return reqs.requests().stream().map(this::saveOne).toList();
    }

    private AddProblemResponse saveOne(AddProblemRequest req) {
        // 1.  저장하기 전에 제목이 이미 있는지 확인
        if (problemRepository.existsByTitle(req.title())) {
            throw new ProblemAlreadyExistsException();
        }

        // 2. 토픽 확인
        Topic topic = topicRepository.findById(req.topicId())
                .orElseThrow(TopicNotFoundException::new);

        // 3. 문제 엔티티 저장
        Problem problem = Problem.builder()
                .topic(topic)
                .title(req.title())
                .difficulty(req.difficulty())
                .difficultyOrder(req.difficultyOrder())
                .description(req.description())
                .constraints(req.constraints())
                .lineSolution(req.lineSolution())
                .solutionText(req.solutionText())
                .build();

        Problem saved = problemRepository.save(problem);


        // 4. 테스트케이스 일괄 저장
        List<TestCase> testCases = req.testCases().stream()
                .map(tc -> TestCase.builder().problem(saved).input(tc.input()).output(tc.output()).build())
                .toList();
        testCaseRepository.saveAll(testCases);

        // 5. 모범 답안 일괄 저장
        Map<Long, Language> languageMap = languageService.validateAndGetLanguageMap(
                req.languageSettings().stream().map(AddLanguageSettingRequest::languageId).toList());

        List<SolutionCode> codes = req.languageSettings().stream()
                .map(sc -> SolutionCode.builder()
                        .problem(saved).language(languageMap.get(sc.languageId())).code(sc.code()).build())
                .toList();
        solutionCodeRepository.saveAll(codes);

        // 6. 시간 제한 일괄 저장
        List<TimeLimit> timeLimits = req.languageSettings().stream()
                .map(t -> TimeLimit.builder()
                        .problem(saved)
                        .language(languageMap.get(t.languageId()))
                        .timeLimitMs(t.timeLimitMs())
                        .build())
                .toList();
        timeLimitRepository.saveAll(timeLimits);

        return AddProblemResponse.builder()
                .problemId(saved.getId())
                .topicId(topic.getId())
                .title(saved.getTitle())
                .difficulty(saved.getDifficulty())
                .difficultyOrder(saved.getDifficultyOrder())
                .testCaseCount(testCases.size())
                .languageSettingCount(codes.size())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ProblemListResponseDTO getProblemListByTopic(Long topicId, Long userId) {
        // 사용자가 있는지 먼저 확인
        userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        // 해당 토픽이 존재하는지 먼저 확인 -> 없으면 에러남
        if (!topicRepository.existsById(topicId)) {
            throw new TopicNotFoundException();
        }

        List<Problem> problems = problemRepository.findAllByTopicIdOrderByDifficultyOrderAscIdAsc(topicId);

        List<ProblemResponseDTO> resultDTOs = problems.stream()
                .map(problem -> {
                    String displayName = switch (problem.getDifficulty().toUpperCase()) {
                        case "EASY" -> "쉬움";
                        case "NORMAL" -> "보통";
                        case "HARD" -> "어려움";
                        default -> "미정";
                    };

                    // 맞췄던 문제인지 (언어 무관)
                    boolean isCompleted = historyRepository.existsByUser_IdAndProblem_IdAndStatus(userId, problem.getId(), HistoryStatus.ACCEPTED);
                    // 해당 문제를 찜해놓은 사람 수
                    int bookmarkCount = bookmarkRepository.countByProblem_Id(problem.getId());
                    // 사용자는 해당 문제를 찜했는지
                    boolean isBookmarked = bookmarkRepository.existsByUser_IdAndProblem_Id(userId, problem.getId());

                    return ProblemResponseDTO.builder()
                            .problemId(problem.getId())
                            .title(problem.getTitle())
                            .difficulty(problem.getDifficulty())
                            .difficultyDisplayName(displayName)
                            .isCompleted(isCompleted)
                            .bookmarkCount(bookmarkCount)
                            .isBookmark(isBookmarked)
                            .build();
                })
                .toList();

        return ProblemListResponseDTO.builder()
                .topicId(topicId)
                .count(resultDTOs.size())
                .problems(resultDTOs)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ProblemAllResponseDTO.ProblemListResponse getProblemList(Long userId, String difficulty, Pageable pageable) {

        // 1. 사용자 존재 여부 확인
        userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if (difficulty != null && !difficulty.isBlank() && !difficulty.equalsIgnoreCase("ALL")) {
            String upperDiff = difficulty.toUpperCase();
            if (!List.of("EASY", "NORMAL", "HARD").contains(upperDiff)) {
                throw new ProblemInvalidDifficultyException();
            }
        }

        // 2. 난이도 필터링 분기 처리
        Page<Problem> problemPage;
        if (difficulty == null || difficulty.isBlank() || difficulty.equalsIgnoreCase("ALL")) {
            // 전체 조회 (난이도 상관없이 모든 문제)
            problemPage = problemRepository.findAll(pageable);
        } else {
            // 특정 난이도 필터링 (EASY, NORMAL, HARD 등)
            problemPage = problemRepository.findAllByDifficulty(difficulty.toUpperCase(), pageable);
        }

        List<ProblemAllResponseDTO.ProblemItemDTO> problemItems = problemPage.getContent().stream()
                .map(problem -> {
                    // 난이도 한글 표시명 변환
                    String difficultyDisplayName = switch (problem.getDifficulty().toUpperCase()) {
                        case "EASY" -> "쉬움";
                        case "NORMAL" -> "보통";
                        case "HARD" -> "어려움";
                        default -> "미정";
                    };

                    return ProblemAllResponseDTO.ProblemItemDTO.builder()
                            .problemId(problem.getId())
                            .title(problem.getTitle())
                            .difficulty(problem.getDifficulty())
                            .difficultyDisplayName(difficultyDisplayName)
                            .bookmarkCount((long) bookmarkRepository.countByProblem_Id(problem.getId()))
                            .isBookmark(bookmarkRepository.existsByUser_IdAndProblem_Id(userId, problem.getId()))
                            .topicName(problem.getTopic() != null ? problem.getTopic().getName() : "NONE")
                            .topicDisplayName(problem.getTopic() != null ? problem.getTopic().getDisplayName() : "미분류")
                            .isCompleted(historyRepository.existsByUser_IdAndProblem_IdAndStatus(userId, problem.getId(), HistoryStatus.ACCEPTED))
                            .build();
                }).toList();

        // 4. 최종 페이징 정보와 함께 반환
        return ProblemAllResponseDTO.ProblemListResponse.builder()
                .problemList(problemItems)
                .page(problemPage.getNumber())
                .size(problemPage.getSize())
                .totalPage(problemPage.getTotalPages())
                .totalElements(problemPage.getTotalElements())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ProblemDetailResponseDTO getProblemDetail(Long userId, Long problemId, String languageName) {
        // 1. 사용자 조회
        userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        // 2. 문제 엔티티 조회
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(ProblemNotFoundException::new);

        // 3. 해당 언어 조회
        Language language = languageService.findLanguageWithName(languageName);

        // 4. 테스트 케이스 (최대 2개만)
        List<TestCaseDTO> testCaseDTOs = problem.getTestCases().stream()
                .sorted(Comparator.comparing(TestCase::getId))
                .limit(2)
                .map(tc -> TestCaseDTO.builder()
                        .input(tc.getInput())
                        .output(tc.getOutput())
                        .build())
                .toList();

        // 5. 부가 정보 조회 (성공 여부, 북마크 수, 내 북마크 여부)
        boolean isCompleted = historyRepository.existsByUser_IdAndProblem_IdAndStatus(userId, problemId, HistoryStatus.ACCEPTED);
        int bookmarkCount = bookmarkRepository.countByProblem_Id(problemId);
        boolean isBookmarked = bookmarkRepository.existsByUser_IdAndProblem_Id(userId, problemId);

        // 6. 시간 제한
        double timeLimitSec = timeLimitRepository.findByProblem_IdAndLanguage_Id(problemId, language.getId())
                .map(t -> t.getTimeLimitMs() / 1000.0)
                .orElse(1.0);

        // 7. 최종 DTO 조립
        return ProblemDetailResponseDTO.builder()
                .problemId(problem.getId())
                .title(problem.getTitle())
                .description(problem.getDescription())
                .constraint(problem.getConstraints())
                .testCases(testCaseDTOs)
                .isCompleted(isCompleted)
                .bookmarkCount(bookmarkCount)
                .isBookmark(isBookmarked)
                .timeLimit(timeLimitSec)
                .memoryLimit(256000)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ProblemSolutionResponseDTO getProblemSolution(Long problemId, String languageName) {

        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(ProblemNotFoundException::new);

        SolutionCode solutionCode = problem.getSolutionCodes().stream()
                .filter(sc -> sc.getLanguage().getName().equalsIgnoreCase(languageName))
                .findFirst()
                .orElseThrow(ProblemSolutionNotFoundException::new);

        return ProblemSolutionResponseDTO.builder()
                .lineSolution(problem.getLineSolution())
                .solutionText(problem.getSolutionText())
                .language(solutionCode.getLanguage().getName())
                .solutionCode(solutionCode.getCode())
                .build();
    }

    @Override
    public List<ProblemHistoryResponse> getProblemHistory(Long userId, Long problemId) {
        userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        problemRepository.findById(problemId).orElseThrow(ProblemNotFoundException::new);

        return historyRepository.findByUser_IdAndProblem_IdOrderByCreatedAtDesc(userId, problemId)
                .stream()
                .map(ProblemHistoryResponse::from)
                .toList();
    }


    @Override
    public AddProblemLanguageSettingResponse addProblemLanguageSetting(Long problemId, Long languageId, AddProblemLanguageSettingRequest req) {
        // 해당 문제가 DB에 있는지
        Problem problem = problemRepository.findById(problemId).orElseThrow(ProblemNotFoundException::new);
        // 해당 언어가 DB에 있는지
        Language language = languageService.findLanguageWithId(languageId);

        if (timeLimitRepository.existsByProblem_IdAndLanguage_Id(problemId, languageId)) {
            throw new ProblemLanguageTimeLimitAlreadyExistsException();
        }
        if (solutionCodeRepository.existsByProblem_IdAndLanguage_Id(problemId, languageId)) {
            throw new ProblemLanguageSolutionCodeAlreadyExistsException();
        }

        TimeLimit timeLimit = TimeLimit.builder()
                .timeLimitMs(req.timeLimitMs())
                .problem(problem)
                .language(language)
                .build();
        TimeLimit savedTimeLimit = timeLimitRepository.save(timeLimit);

        SolutionCode solutionCode = SolutionCode.builder()
                .code(req.solutionCode())
                .problem(problem)
                .language(language)
                .build();
        SolutionCode savedSolutionCode = solutionCodeRepository.save(solutionCode);

        return AddProblemLanguageSettingResponse.builder()
                .languageName(language.getName())
                .timeLimitId(savedTimeLimit.getId())
                .solutionCodeId(savedSolutionCode.getId())
                .build();
    }

    @Override
    public TempStorageResponseDTO saveOrUpdateTempCode(Long userId, Long problemId, String language, ProblemRequestDTO.TempStorageRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());

        if (!problemRepository.existsById(problemId)) {
            throw new ProblemNotFoundException();
        }

        Language languageEntity = languageService.findLanguageWithName(language);

        UserProblemCode userCode = userProblemCodeRepository
                .findByUserAndProblemIdAndLanguage(user, problemId, languageEntity)
                .map(existingCode -> {
                    existingCode.updateSourceCode(request.getSourceCode());
                    return existingCode;
                })
                .orElseGet(() -> UserProblemCode.builder()
                        .user(user)
                        .problemId(problemId)
                        .language(languageEntity)
                        .sourceCode(request.getSourceCode())
                        .build());

        UserProblemCode saved = userProblemCodeRepository.save(userCode);

        return TempStorageResponseDTO.builder()
                .userCodeId(saved.getId())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public TempStorageGetDTO getTempCode(Long userId, Long problemId, String language) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());
        if (!problemRepository.existsById(problemId)) {
            throw new ProblemNotFoundException();
        }

        Language languageEntity = languageService.findLanguageWithName(language);

        return userProblemCodeRepository.findByUserAndProblemIdAndLanguage(user, problemId, languageEntity)
                .map(code -> TempStorageGetDTO.builder()
                        .userCodeId(code.getId())
                        .sourceCode(code.getSourceCode())
                        .language(code.getLanguage().getName())
                        .updatedAt(code.getUpdatedAt())
                        .build())
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecentHistoryResponseDTO> getRecentHistories(Long userId) {

        userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        List<History> histories = historyRepository.findTop10ByUser_IdOrderByCreatedAtDesc(userId);

        if (histories == null || histories.isEmpty()) {
            return new ArrayList<>();
        }

        return histories.stream()
                .map(history -> RecentHistoryResponseDTO.builder()
                        .historyId(history.getId())
                        .problemId(history.getProblem().getId())
                        .title(history.getProblem().getTitle())
                        .sourceCode(history.getSourceCode())
                        .status(history.getStatus().name())
                        .language(history.getLanguage().getName())
                        .createdAt(LocalDateTime.ofInstant(history.getCreatedAt(), ZoneId.of("Asia/Seoul")))
                        .build())
                .toList();
    }
    }

