package com.pocketco.domain.problem.application;

import com.pocketco.domain.admin.dto.AddProblemRequest;
import com.pocketco.domain.admin.dto.AddProblemResponse;
import java.util.List;

public interface ProblemService {
    List<AddProblemResponse> addProblems(List<AddProblemRequest> requests);
}