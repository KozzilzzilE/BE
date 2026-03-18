package com.pocketco.domain.admin.application;

import com.pocketco.domain.admin.dto.*;
import com.pocketco.domain.admin.exception.NotAdminException;
import com.pocketco.domain.judge0.application.Judge0Service;
import com.pocketco.domain.judge0.dto.Judge0LanguageResponse;
import com.pocketco.domain.language.application.LanguageService;
import com.pocketco.domain.learning.application.AppliedService;
import com.pocketco.domain.learning.application.NotionService;
import com.pocketco.domain.topic.application.TopicService;
import com.pocketco.domain.user.entity.Role;
import com.pocketco.domain.user.entity.User;
import com.pocketco.domain.user.exception.UserNotFoundException;
import com.pocketco.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.pocketco.domain.problem.application.ProblemService;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final LanguageService languageService;
    private final TopicService topicService;
    private final NotionService notionService;
    private final AppliedService appliedService;
    private final ProblemService problemService;
    private final Judge0Service judge0Service;

    private void validateAdmin(Long userId) {
        User me = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if (me.getRole() != Role.ADMIN) {
            throw new NotAdminException();
        }
    }

    @Override
    public AddLanguageResponse addLanguage(Long userId, AddLanguageRequest request) {
        validateAdmin(userId);
        return languageService.addLanguage(request);
    }

    @Override
    public AddTopicResponse addTopic(Long userId, AddTopicRequest request) {
        validateAdmin(userId);
        return topicService.addTopic(request);
    }

    @Override
    public AddNotionResponse addNotion(Long userId, MultipartFile image, AddNotionRequest request) throws IOException {
        validateAdmin(userId);
        return notionService.addNotion(image, request);
    }

    @Override
    public List<AddAppliedResponse> addApplied(Long userId, List<AddAppliedRequest> requests) {
        validateAdmin(userId);
        return appliedService.addApplied(requests);
    }
    @Override
    public List<AddProblemResponse> addProblems(Long userId, List<AddProblemRequest> requests) {
        validateAdmin(userId); // 관리자 확인 로직 재사용
        return problemService.addProblems(requests);
    }

    @Override
    public List<Judge0LanguageResponse> judge0Languages(Long userId) {
        validateAdmin(userId);
        return judge0Service.getJudge0Languages();
    }
}
