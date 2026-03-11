package com.pocketco.domain.problem.api;

import com.pocketco.domain.problem.application.ProblemService;
import com.pocketco.domain.problem.dto.ProblemDetailResponseDTO;
import com.pocketco.domain.problem.dto.ProblemSolutionResponseDTO;
import com.pocketco.global.common.response.BaseResponse;
import com.pocketco.global.common.code.status.SuccessStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/problems")
public class ProblemController {

    private final ProblemService problemService;

    @GetMapping("/{problemId}")
    public BaseResponse<ProblemDetailResponseDTO> getProblemDetail(
            @PathVariable(name = "problemId") Long problemId) {

        return BaseResponse.onSuccess(
                SuccessStatus.PROBLEM_DETAIL_GET_SUCCESS,
                problemService.getProblemDetail(problemId)
        );
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
}