package com.pocketco.domain.problem.application;

import com.pocketco.domain.admin.dto.*;
import com.pocketco.domain.language.application.LanguageService;
import com.pocketco.domain.language.entity.Language;
import com.pocketco.domain.problem.entity.*;
import com.pocketco.domain.problem.repository.*;
import com.pocketco.domain.topic.entity.Topic;
import com.pocketco.domain.topic.exception.TopicNotFoundException;
import com.pocketco.domain.topic.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.pocketco.global.exception.GeneralException; // 패키지 경로 확인 필요
import com.pocketco.global.common.code.status.ErrorStatus;
import com.pocketco.domain.problem.exception.ProblemHandler;

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
        // 1. [중복 체크] 저장하기 전에 제목이 이미 있는지 확인!
        if (problemRepository.existsByTitle(req.title())) {
            // GeneralException 대신 ProblemHandler를 던집니다!
            throw new ProblemHandler(ErrorStatus.PROBLEM_ALREADY_EXISTS);
        }

        // 2. 토픽 확인
        Topic topic = topicRepository.findById(req.topicId())
                .orElseThrow(TopicNotFoundException::new);

        // 3. 문제 엔티티 저장 (여기서부터는 기존 코드와 동일)
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

        // ... (이하 테스트케이스, 솔루션 코드 저장 로직은 그대로 두시면 됩니다)

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
    }}