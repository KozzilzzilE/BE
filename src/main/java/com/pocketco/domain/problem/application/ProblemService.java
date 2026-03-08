package com.pocketco.domain.problem.application;

import com.pocketco.domain.admin.dto.AddProblemRequest;
import com.pocketco.domain.admin.dto.AddProblemResponse;
import com.pocketco.domain.problem.dto.ProblemListResponseDTO;

import java.util.List;

public interface ProblemService {
    List<AddProblemResponse> addProblems(List<AddProblemRequest> requests);

    ProblemListResponseDTO getProblemListByTopic(Long topicId);
}