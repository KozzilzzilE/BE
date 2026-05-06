package com.pocketco.domain.problem.application;

import com.pocketco.domain.admin.dto.*;
import com.pocketco.domain.problem.dto.ProblemHistoryResponse;
import com.pocketco.domain.problem.dto.ProblemListResponseDTO;
import com.pocketco.domain.problem.dto.ProblemDetailResponseDTO;
import com.pocketco.domain.problem.dto.ProblemSolutionResponseDTO;
import org.springframework.data.domain.Pageable;
import com.pocketco.domain.problem.dto.*;
import com.pocketco.domain.user.entity.User;

import java.util.List;

public interface ProblemService {
    List<AddProblemResponse> addProblems(AddProblemRequests reqs);
    ProblemListResponseDTO getProblemListByTopic(Long topicId, Long userId);
    ProblemDetailResponseDTO getProblemDetail(Long userId, Long problemId, String languageName);
    ProblemSolutionResponseDTO getProblemSolution(Long problemId, String language);
    List<ProblemHistoryResponse> getProblemHistory(Long userId, Long problemId);
    AddProblemLanguageSettingResponse addProblemLanguageSetting(Long problemId, Long languageId, AddProblemLanguageSettingRequest req);
    ProblemAllResponseDTO.ProblemListResponse getProblemList(Long userId, String difficulty, Pageable pageable);
    TempStorageResponseDTO saveOrUpdateTempCode(Long userId, Long problemId, String language, ProblemRequestDTO.TempStorageRequest request);
    TempStorageGetDTO getTempCode(Long userId, Long problemId, String language);
    // ProblemService.java
    List<RecentHistoryResponseDTO> getRecentHistories(Long userId);
}