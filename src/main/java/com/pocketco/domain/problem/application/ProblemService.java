package com.pocketco.domain.problem.application;

import com.pocketco.domain.admin.dto.AddProblemRequest;
import com.pocketco.domain.admin.dto.AddProblemResponse;
import com.pocketco.domain.problem.dto.ProblemHistoryResponse;
import com.pocketco.domain.problem.dto.ProblemListResponseDTO;
import com.pocketco.domain.problem.dto.ProblemDetailResponseDTO;
import com.pocketco.domain.problem.dto.ProblemSolutionResponseDTO;

import java.util.List;

public interface ProblemService {
    List<AddProblemResponse> addProblems(List<AddProblemRequest> requests);
    ProblemListResponseDTO getProblemListByTopic(Long topicId);
    ProblemDetailResponseDTO getProblemDetail(Long userId, Long problemId, String languageName);
    ProblemSolutionResponseDTO getProblemSolution(Long problemId, String language);
    List<ProblemHistoryResponse> getProblemHistory(Long userId, Long problemId);
}