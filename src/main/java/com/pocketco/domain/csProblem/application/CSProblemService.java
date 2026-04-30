package com.pocketco.domain.csProblem.application;

import com.pocketco.domain.admin.dto.AddCSProblemRequest;
import com.pocketco.domain.csProblem.dto.CSProblemRandomResponseDTO;

import java.util.List;

public interface CSProblemService {
    Long addCSProblem(AddCSProblemRequest request);
    List<CSProblemRandomResponseDTO> getRandomCSProblems(int count);
}