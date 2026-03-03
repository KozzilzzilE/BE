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
        Topic topic = topicRepository.findById(req.topicId()).orElseThrow(TopicNotFoundException::new);

        // 1. 문제 엔티티 저장
        Problem problem = Problem.builder()
                .topic(topic).title(req.title()).difficulty(req.difficulty())
                .difficultyOrder(req.difficultyOrder()).description(req.description())
                .constraints(req.constraints()).lineSolution(req.lineSolution())
                .solutionText(req.solutionText()).build();
        Problem saved = problemRepository.save(problem);

        // 2. 테스트케이스 일괄 저장
        List<TestCase> testCases = req.testCases().stream()
                .map(tc -> TestCase.builder().problem(saved).input(tc.input()).output(tc.output()).build())
                .toList();
        testCaseRepository.saveAll(testCases);

        // 3. 모범 답안 일괄 저장 (Language 검증)
        Map<Long, Language> languageMap = languageService.validateAndGetLanguageMap(
                req.solutionCodes().stream().map(AddSolutionCodeRequest::languageId).toList());

        List<SolutionCode> codes = req.solutionCodes().stream()
                .map(sc -> SolutionCode.builder()
                        .problem(saved).language(languageMap.get(sc.languageId())).code(sc.code()).build())
                .toList();
        solutionCodeRepository.saveAll(codes);

        return AddProblemResponse.builder()
                .problemId(saved.getId()).topicId(topic.getId()).title(saved.getTitle())
                .difficulty(saved.getDifficulty()).difficultyOrder(saved.getDifficultyOrder())
                .testCaseCount(testCases.size()).solutionCodeCount(codes.size()).build();
    }
}