package com.pocketco.domain.problem.application;

import com.pocketco.domain.admin.dto.*;
import com.pocketco.domain.language.application.LanguageService;
import com.pocketco.domain.language.entity.Language;
import com.pocketco.domain.problem.dto.ProblemListResponseDTO;
import com.pocketco.domain.problem.dto.ProblemResponseDTO;
import com.pocketco.domain.problem.dto.*;
import com.pocketco.domain.problem.entity.*;
import com.pocketco.domain.problem.repository.*;
import com.pocketco.domain.topic.entity.Topic;
import com.pocketco.domain.topic.exception.TopicNotFoundException;
import com.pocketco.domain.topic.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.pocketco.global.exception.GeneralException;
import com.pocketco.global.common.code.status.ErrorStatus;
import com.pocketco.domain.problem.exception.ProblemHandler;
import java.util.Comparator;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class ProblemServiceImpl implements ProblemService {
    private final ProblemRepository problemRepository;
    private final TestCaseRepository testCaseRepository;
    private final SolutionCodeRepository solutionCodeRepository;
    private final TopicRepository topicRepository;
    private final LanguageService languageService;

    @Override
    public List<AddProblemResponse> addProblems(List<AddProblemRequest> requests) {
        return requests.stream().map(this::saveOne).toList();
    }

    private AddProblemResponse saveOne(AddProblemRequest req) {
        // 1.  저장하기 전에 제목이 이미 있는지 확인
        if (problemRepository.existsByTitle(req.title())) {
            throw new ProblemHandler(ErrorStatus.PROBLEM_ALREADY_EXISTS);
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
                req.solutionCodes().stream().map(AddSolutionCodeRequest::languageId).toList());

        List<SolutionCode> codes = req.solutionCodes().stream()
                .map(sc -> SolutionCode.builder()
                        .problem(saved).language(languageMap.get(sc.languageId())).code(sc.code()).build())
                .toList();
        solutionCodeRepository.saveAll(codes);

        return AddProblemResponse.builder()
                .problemId(saved.getId())
                .topicId(topic.getId())
                .title(saved.getTitle())
                .difficulty(saved.getDifficulty())
                .difficultyOrder(saved.getDifficultyOrder())
                .testCaseCount(testCases.size())
                .solutionCodeCount(codes.size())
                .build();
    }
    @Override
    @Transactional(readOnly = true)
    public ProblemListResponseDTO getProblemListByTopic(Long topicId) {

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

                    return ProblemResponseDTO.builder()
                            .problemId(problem.getId())
                            .title(problem.getTitle())
                            .difficulty(problem.getDifficulty())
                            .difficultyDisplayName(displayName)
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
    public ProblemDetailResponseDTO getProblemDetail(Long problemId) {
        // 1. 문제 엔티티 조회
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new ProblemHandler(ErrorStatus.PROBLEM_NOT_FOUND));

        // 2. 테스트 케이스 변환 (명세서대로 최대 2개만 추출)
        List<TestCaseDTO> testCaseDTOs = problem.getTestCases().stream()
                .sorted(Comparator.comparing(TestCase::getId))
                .limit(2)
                .map(tc -> TestCaseDTO.builder()
                        .input(tc.getInput())
                        .output(tc.getOutput())
                        .build())
                .toList();

        System.out.println("--- [상세조회] 화면에 보여줄 테스트케이스 ---");
        testCaseDTOs.forEach(tc -> System.out.println("입력: " + tc.input()));

        // 3. 최종 DTO 조립
        return ProblemDetailResponseDTO.builder()
                .exerciseId(problem.getId())
                .title(problem.getTitle())
                .description(problem.getDescription())
                .constraint(problem.getConstraints())
                .testCases(testCaseDTOs)
                .isCompleted(false) // 아직 연동 전이라 기본값 false
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ProblemSolutionResponseDTO getProblemSolution(Long problemId, String languageName) { // 👈 Long languageId를 String languageName으로 변경!
        // 1. 문제 엔티티 조회
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new ProblemHandler(ErrorStatus.PROBLEM_NOT_FOUND));

        // 2. 완수 오빠 피드백 반영: 문제에 해당 언어의 답안이 있는지 확인
        SolutionCode solutionCode = problem.getSolutionCodes().stream()
                .filter(sc -> sc.getLanguage().getName().equalsIgnoreCase(languageName)) // ✨ 이름으로 필터링
                .findFirst()
                .orElseThrow(() -> new ProblemHandler(ErrorStatus.SOLUTION_NOT_FOUND)); // 👈 오빠가 말한 예외 처리 디테일!

        // 3. 답안 DTO 반환
        return ProblemSolutionResponseDTO.builder()
                .lineSolution(problem.getLineSolution())
                .solutionText(problem.getSolutionText())
                .language(solutionCode.getLanguage().getName())
                .solutionCode(solutionCode.getCode())
                .build();
    }
}




