package com.pocketco.domain.admin.application;

import com.pocketco.domain.admin.dto.*;
import com.pocketco.domain.judge0.dto.Judge0LanguageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AdminService {
    AddLanguageResponse addLanguage(Long userId, AddLanguageRequest request);
    AddTopicResponse addTopic(Long userId, AddTopicRequest request);
    AddNotionResponse addNotion(Long userId, MultipartFile image, AddNotionRequest request) throws IOException;
    List<AddAppliedResponse> addApplied(Long userId, List<AddAppliedRequest> request);
    List<AddProblemResponse> addProblems(Long userId, AddProblemRequests requests);
    List<Judge0LanguageResponse> judge0Languages(Long userId);
    AddProblemLanguageSettingResponse addProblemLanguageSetting(Long userId, Long problemId, Long languageId, AddProblemLanguageSettingRequest request);
    Long addCSProblem(Long userId, AddCSProblemRequest request);
    AdminResponseDTO.AddNotionCodeResponse addNotionCode(Long userId, Long notionId, Long languageId, AdminRequestDTO.AddNotionCodeRequest request);
    // AdminService.java
    AddExerciseAppliedCodeResponse addAppliedCode(Long userId, Long exerciseId, Long languageId, AddExerciseAppliedCodeRequest request);

}