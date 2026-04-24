package com.pocketco.domain.problem.api;

import com.pocketco.domain.problem.application.ProblemService;
import com.pocketco.domain.problem.dto.ProblemDetailResponseDTO;
import com.pocketco.domain.problem.dto.ProblemHistoryResponse;
import com.pocketco.domain.problem.dto.ProblemSolutionResponseDTO;
import com.pocketco.global.common.response.BaseResponse;
import com.pocketco.global.common.code.status.SuccessStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/problems")
public class ProblemController {

    private final ProblemService problemService;

    @GetMapping("/{problemId}")
    public BaseResponse<ProblemDetailResponseDTO> getProblemDetail(
            @AuthenticationPrincipal Long userId,
            @PathVariable(name = "problemId") Long problemId,
            @RequestParam(name = "language") String language) {

        ProblemDetailResponseDTO result = problemService.getProblemDetail(userId, problemId, language);

        return BaseResponse.onSuccess(SuccessStatus.PROBLEM_DETAIL_GET_SUCCESS, result);
    }

    @GetMapping("/{problemId}/solutions")
    public BaseResponse<ProblemSolutionResponseDTO> getProblemSolution(
            @PathVariable(name = "problemId") Long problemId,
            @RequestParam(name = "language") String language){

        return BaseResponse.onSuccess(
                SuccessStatus.PROBLEM_SOLUTION_GET_SUCCESS,
                problemService.getProblemSolution(problemId, language)
        );
    }

    @GetMapping("/{problemId}/histories")
    public BaseResponse<List<ProblemHistoryResponse>> getProblemHistories(
            @AuthenticationPrincipal Long userId,
            @PathVariable(name = "problemId") Long problemId) {
        List<ProblemHistoryResponse> result = problemService.getProblemHistory(userId, problemId);
        return BaseResponse.onSuccess(SuccessStatus.PROBLEM_SUBMIT_HISTORY_SUCCESS, result);
    }
}