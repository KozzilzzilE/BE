package com.pocketco.domain.learning.application;

import com.pocketco.domain.admin.dto.AddAppliedBlank;
import com.pocketco.domain.admin.dto.AddAppliedCodeRequest;
import com.pocketco.domain.admin.dto.AddAppliedRequest;
import com.pocketco.domain.admin.dto.AddAppliedResponse;
import com.pocketco.domain.language.application.LanguageService;
import com.pocketco.domain.language.entity.Language;
import com.pocketco.domain.learning.entity.applied.AppliedBlankProblem;
import com.pocketco.domain.learning.entity.applied.AppliedCode;
import com.pocketco.domain.learning.entity.applied.AppliedExercise;
import com.pocketco.domain.learning.exception.AlreadyExistsAppliedExerciseException;
import com.pocketco.domain.learning.repository.applied.AppliedBlankProblemRepository;
import com.pocketco.domain.learning.repository.applied.AppliedCodeRepository;
import com.pocketco.domain.learning.repository.applied.AppliedCompletionRepository;
import com.pocketco.domain.learning.repository.applied.AppliedExerciseRepository;
import com.pocketco.domain.topic.entity.Topic;
import com.pocketco.domain.topic.exception.TopicNotFoundException;
import com.pocketco.domain.topic.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Transactional
public class AppliedServiceImpl implements AppliedService {
    private final AppliedExerciseRepository appliedExerciseRepository;
    private final AppliedCodeRepository appliedCodeRepository;
    private final AppliedBlankProblemRepository appliedBlankProblemRepository;
    private final AppliedCompletionRepository appliedCompletionRepository;
    private final TopicRepository topicRepository;
    private final LanguageService languageService;

    @Override
    public List<AddAppliedResponse> addApplied(List<AddAppliedRequest> reqs) {
        return reqs.stream()
                .map(req -> processSingleApplied(req))
                .toList();
    }

    private AddAppliedResponse processSingleApplied(AddAppliedRequest req) {
        if (appliedExerciseRepository.existsByTopic_IdAndOrderNo(req.topicId(), req.orderNo())) {
            throw new AlreadyExistsAppliedExerciseException();
        }
        Topic topic = topicRepository.findById(req.topicId()).orElseThrow(TopicNotFoundException::new);

        AppliedExercise appliedExercise = AppliedExercise.builder()
                .orderNo(req.orderNo())
                .title(req.title())
                .description(req.description())
                .topic(topic)
                .build();

        AppliedExercise savedAppliedExercise = appliedExerciseRepository.save(appliedExercise);

        // 중복된 언어 및 존재하지 않는 언어 추가 요청을 방지
        Map<Long, Language> languageMap = languageService.validateAndGetLanguageMap(
                req.codes().stream().map(AddAppliedCodeRequest::languageId).toList());

        List<AppliedCode> codes = req.codes().stream()
                .map(reqCode -> AppliedCode.builder()
                        .exercise(savedAppliedExercise)
                        .language(languageMap.get(reqCode.languageId()))
                        .codeTemplate(reqCode.codeTemplate())
                        .build())
                .toList();

        Iterable<AppliedCode> savedIterable = appliedCodeRepository.saveAll(codes);
        List<AppliedCode> savedCodes = StreamSupport.stream(savedIterable.spliterator(), false).toList();

        Map<Long, AppliedCode> savedCodeByLanguageId = savedCodes.stream()
                .collect(Collectors.toMap(c -> c.getLanguage().getId(), Function.identity()));

        List<AppliedBlankProblem> blankEntities = new ArrayList<>();

        for (AddAppliedCodeRequest reqCode : req.codes()) {
            AppliedCode savedCode = savedCodeByLanguageId.get(reqCode.languageId());
            // 안전상 체크 가능
            if (savedCode == null) throw new IllegalStateException("Saved code not found");

            // 추후에 정답(answer 중복이나, 빈칸보다 큰 수 등 다양한 조건들 검증 코드 추가)
            for (AddAppliedBlank blank : reqCode.blanks()) {
                blankEntities.add(AppliedBlankProblem.builder()
                        .exerciseCode(savedCode)
                        .content(blank.content())
                        .answer(blank.answer())
                        .build()
                );
            }
        }

        appliedBlankProblemRepository.saveAll(blankEntities);

        return AddAppliedResponse.builder()
                .exerciseId(savedAppliedExercise.getId())
                .orderNo(savedAppliedExercise.getOrderNo())
                .title(savedAppliedExercise.getTitle())
                .build();
    }
}