package com.pocketco.domain.admin.application;

import com.pocketco.domain.admin.dto.*;
import com.pocketco.domain.csProblem.application.CSProblemService;
import com.pocketco.domain.judge0.application.Judge0Service;
import com.pocketco.domain.judge0.dto.Judge0LanguageResponse;
import com.pocketco.domain.language.application.LanguageService;
import com.pocketco.domain.learning.application.AppliedService;
import com.pocketco.domain.learning.application.NotionService;
import com.pocketco.domain.topic.application.TopicService;
import com.pocketco.domain.user.application.PublicProfileImageService;
import com.pocketco.domain.user.application.UserService;
import com.pocketco.domain.user.repository.UserRepository;
import com.pocketco.domain.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.pocketco.domain.problem.application.ProblemService;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {
    private final LanguageService languageService;
    private final TopicService topicService;
    private final NotionService notionService;
    private final AppliedService appliedService;
    private final ProblemService problemService;
    private final Judge0Service judge0Service;
    private final CSProblemService csProblemService;
    private final PublicProfileImageService publicProfileImageService;
    private final UserRepository userRepository;

    @Override
    public AddLanguageResponse addLanguage(Long userId, AddLanguageRequest request) {
        return languageService.addLanguage(request);
    }

    @Override
    public AddTopicResponse addTopic(Long userId, AddTopicRequest request) {
        return topicService.addTopic(request);
    }

    @Override
    public AddNotionResponse addNotion(Long userId, MultipartFile image, AddNotionRequest request) throws IOException {
        return notionService.addNotion(image, request);
    }

    @Override
    public List<AddAppliedResponse> addApplied(Long userId, List<AddAppliedRequest> requests) {
        return appliedService.addApplied(requests);
    }
    @Override
    public List<AddProblemResponse> addProblems(Long userId, AddProblemRequests requests) {
        return problemService.addProblems(requests);
    }

    @Override
    public List<Judge0LanguageResponse> judge0Languages(Long userId) {
        return judge0Service.getJudge0Languages();
    }

    @Override
    public AddProblemLanguageSettingResponse addProblemLanguageSetting(Long userId, Long problemId, Long languageId, AddProblemLanguageSettingRequest request) {
        return problemService.addProblemLanguageSetting(problemId, languageId, request);
    }

    @Override
    public Long addCSProblem(Long userId, AddCSProblemRequest request) {
        return csProblemService.addCSProblem(request);
    }
    @Override
    public AdminResponseDTO.AddNotionCodeResponse addNotionCode(Long userId, Long notionId, Long languageId, AdminRequestDTO.AddNotionCodeRequest request) {
        return notionService.addNotionCode(notionId, languageId, request);
    }

    @Override
    public AddExerciseAppliedCodeResponse addAppliedCode(Long userId, Long exerciseId, Long languageId, AddExerciseAppliedCodeRequest request) {
        userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        return appliedService.addAppliedCode(exerciseId, languageId, request);
    }

    @Override
    public AddPublicProfileImageResponse addPublicProfileImage(Long userId, MultipartFile image) throws IOException {
        userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        return publicProfileImageService.addPublicProfileImage(image);
    }
}