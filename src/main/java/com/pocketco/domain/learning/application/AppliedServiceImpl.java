package com.pocketco.domain.learning.application;

import com.pocketco.domain.admin.dto.*;
import com.pocketco.domain.admin.dto.AddAppliedBlank;
import com.pocketco.domain.admin.dto.AddAppliedCodeRequest;
import com.pocketco.domain.language.repository.LanguageRepository;
import com.pocketco.domain.admin.dto.AddAppliedRequest;
import com.pocketco.domain.admin.dto.AddAppliedResponse;
import com.pocketco.domain.language.application.LanguageService;
import com.pocketco.domain.language.entity.Language;
import com.pocketco.domain.learning.converter.LearningAppliedExerciseConverter;
import com.pocketco.domain.learning.dto.LearningAppliedCompletionResponse;
import com.pocketco.domain.learning.repository.applied.*;
import com.pocketco.domain.learning.dto.LearningAppliedExercise;
import com.pocketco.domain.learning.dto.LearningAppliedExerciseResponse;
import com.pocketco.domain.learning.entity.applied.AppliedBlankProblem;
import com.pocketco.domain.learning.entity.applied.AppliedCode;
import com.pocketco.domain.learning.entity.applied.AppliedCompletion;
import com.pocketco.domain.learning.entity.applied.AppliedExercise;
import com.pocketco.domain.learning.exception.AlreadyExistsAppliedExerciseException;
import com.pocketco.domain.learning.exception.AppliedExerciseNotExistsException;
import com.pocketco.domain.learning.exception.AppliedExerciseTopicNotExistsException;
import com.pocketco.domain.learning.repository.applied.AppliedBlankProblemRepository;
import com.pocketco.domain.learning.repository.applied.AppliedCodeRepository;
import com.pocketco.domain.learning.repository.applied.AppliedCompletionRepository;
import com.pocketco.domain.learning.repository.applied.AppliedExerciseRepository;
import com.pocketco.domain.learning.exception.AppliedCodeAnswerDuplicateException;
import com.pocketco.domain.topic.entity.Topic;
import com.pocketco.domain.topic.exception.TopicNotFoundException;
import com.pocketco.domain.topic.repository.TopicRepository;
import com.pocketco.domain.user.entity.User;
import com.pocketco.domain.user.exception.UserNotFoundException;
import com.pocketco.domain.learning.exception.AlreadyExistsAppliedCodeException;
import com.pocketco.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.pocketco.domain.language.exception.LanguageNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    private final UserRepository userRepository;
    private final TopicRepository topicRepository;
    private final LanguageService languageService;
    private final LanguageRepository languageRepository;


    @Override
    public List<AddAppliedResponse> addApplied(List<AddAppliedRequest> reqs) {
        return reqs.stream()
                .map(req -> processSingleApplied(req))
                .toList();
    }

    @Override
    public LearningAppliedExerciseResponse getLearningAppliedExercise(Long topicId, String language, Long userId) {
        Long languageId = languageService.getLanguageId(language);
        topicRepository.findById(topicId).orElseThrow(() -> new TopicNotFoundException());

        List<AppliedExercise> exercises = appliedExerciseRepository.findByTopic_IdOrderByOrderNoAsc(topicId);
        if (exercises.isEmpty()) {
            throw new AppliedExerciseTopicNotExistsException();
        }

        List<Long> exerciseIds = exercises.stream().map(AppliedExercise::getId).toList();
        List<AppliedCode> appliedCodes = appliedCodeRepository.findByExercise_IdInAndLanguage_Id(exerciseIds, languageId);
        List<Long> codeIds = appliedCodes.stream().map(AppliedCode::getId).toList();
        List<AppliedBlankProblem> blanks = appliedBlankProblemRepository.findByExerciseCode_IdIn(codeIds);
        List<AppliedCompletion> completions = appliedCompletionRepository.findByExercise_IdInAndUser_Id(exerciseIds, userId);

        Map<Long, AppliedCode> appliedCodeMap = appliedCodes.stream()
                .collect(Collectors.toMap(
                        code -> code.getExercise().getId(),
                        Function.identity()));

        Map<Long, List<AppliedBlankProblem>> appliedBlankMap = blanks.stream()
                .collect(Collectors.groupingBy(blank -> blank.getExerciseCode().getId()));

        Set<Long> completedAppliedIds = completions.stream()
                .map(completion -> completion.getExercise().getId())
                .collect(Collectors.toSet());

        List<LearningAppliedExercise> lists = exercises.stream()
                .map(exercise -> {
                    Long exerciseId = exercise.getId();

                    AppliedCode appliedCode = appliedCodeMap.get(exerciseId);
                    List<AppliedBlankProblem> appliedBlanks = null;
                    if (appliedCode != null) {
                        appliedBlanks = appliedBlankMap.get(appliedCode.getId());
                    }
                    boolean completed = completedAppliedIds.contains(exerciseId);

                    return LearningAppliedExerciseConverter.toExerciseResponse(exercise, appliedCode, completed, appliedBlanks);
                }).toList();

        return LearningAppliedExerciseResponse.builder()
                .count(lists.size())
                .appliedExercises(lists)
                .build();
    }

    @Override
    public LearningAppliedCompletionResponse AppliedComplete(Long exerciseId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        AppliedExercise appliedExercise = appliedExerciseRepository.findById(exerciseId).orElseThrow(AppliedExerciseNotExistsException::new);

        AppliedCompletion completion = AppliedCompletion.builder()
                .exercise(appliedExercise)
                .user(user)
                .build();
        appliedCompletionRepository.save(completion);

        return LearningAppliedCompletionResponse.builder()
                .exerciseId(exerciseId)
                .userName(user.getNickname())
                .appliedCompleted(true)
                .build();
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

    @Override
    public AddExerciseAppliedCodeResponse addAppliedCode(Long exerciseId, Long languageId, AddExerciseAppliedCodeRequest request) {

        //  정답 번호(answer) 중복 체크 로직
        List<Integer> answers = request.getBlanks().stream()
                .map(AddAppliedBlank::answer)
                .filter(java.util.Objects::nonNull)
                .toList();

        long uniqueCount = answers.stream().distinct().count();
        if (answers.size() != uniqueCount) {
            throw new AppliedCodeAnswerDuplicateException();
        }


        // 1. 응용 학습 존재 확인 (
        AppliedExercise exercise = appliedExerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new AppliedExerciseNotExistsException());

        // 2. 언어 존재 확인
        Language language = languageRepository.findById(languageId)
                .orElseThrow(() -> new LanguageNotFoundException());

        // 3. 중복 체크
        if (appliedCodeRepository.existsByExerciseAndLanguage(exercise, language)) {
            throw new AlreadyExistsAppliedCodeException();
        }

        // 4. AppliedCode 저장
        AppliedCode appliedCode = AppliedCode.builder()
                .exercise(exercise)
                .language(language)
                .codeTemplate(request.getCodeTemplate())
                .build();
        AppliedCode savedCode = appliedCodeRepository.save(appliedCode);

        // 5. AppliedBlankProblem 저장
        List<AppliedBlankProblem> blanks = request.getBlanks().stream()
                .map(dto -> AppliedBlankProblem.builder()
                        .exerciseCode(savedCode)
                        .content(dto.content())
                        .answer(dto.answer())
                        .build())
                .toList();

        appliedBlankProblemRepository.saveAll(blanks);

        return AddExerciseAppliedCodeResponse.builder()
                .exerciseId(exercise.getId())
                .languageName(language.getName())
                .appliedCodeId(savedCode.getId())
                .blankCount(blanks.size())
                .build();
    }
}