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
import com.pocketco.domain.problem.dto.ProblemAllResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.data.domain.Sort;
import com.pocketco.domain.problem.dto.TempStorageResponseDTO;
import com.pocketco.domain.problem.dto.ProblemRequestDTO;
import com.pocketco.domain.user.entity.User;
import com.pocketco.domain.problem.dto.TempStorageGetDTO;
import com.pocketco.domain.problem.dto.RecentHistoryResponseDTO;
import com.pocketco.domain.user.exception.UserNotFoundException;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/problems")
public class ProblemController {

    private final ProblemService problemService;

    @GetMapping("/{problemId}")
    public BaseResponse<ProblemDetailResponseDTO> getProblemDetail(
            @AuthenticationPrincipal  Long userId,
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

    @GetMapping("")
    public BaseResponse<ProblemAllResponseDTO.ProblemListResponse> getProblems(
            @AuthenticationPrincipal Long userId,
            @RequestParam(value = "difficulty", required = false, defaultValue = "ALL") String difficulty,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {

        SuccessStatus status = (difficulty == null || difficulty.equalsIgnoreCase("ALL"))
                ? SuccessStatus.PROBLEM_LIST_SUCCESS
                : SuccessStatus.PROBLEM_DIFFICULTY_LIST_SUCCESS;

        return BaseResponse.onSuccess(
                status,
                problemService.getProblemList(userId, difficulty, pageable)
        );
    }

    @PutMapping("/{problemId}/temp-storages")
    public BaseResponse<TempStorageResponseDTO> saveTempCode(
            @PathVariable(name = "problemId") Long problemId,
            @RequestParam(name = "language") String language,
            @RequestBody ProblemRequestDTO.TempStorageRequest request,
            @AuthenticationPrincipal Long userId
    ) {
        TempStorageResponseDTO result = problemService.saveOrUpdateTempCode(userId, problemId, language, request);

        return BaseResponse.onSuccess(SuccessStatus.PROBLEM_TEMP_SAVE_SUCCESS, result);
    }
    @GetMapping("/{problemId}/temp-storages")
    public BaseResponse<TempStorageGetDTO> getTempCode(
            @PathVariable(name = "problemId") Long problemId,
            @RequestParam(name = "language") String language,
            @AuthenticationPrincipal Long userId
    ) {
        TempStorageGetDTO result = problemService.getTempCode(userId, problemId, language);

        return BaseResponse.onSuccess(SuccessStatus.PROBLEM_TEMP_GET_SUCCESS, result);
    }

    @GetMapping("/recent-histories")
    public BaseResponse<List<RecentHistoryResponseDTO>> getRecentHistories(
            @AuthenticationPrincipal Long userId
    ) {
        if (userId == null) {
            throw new UserNotFoundException();
        }

        return BaseResponse.onSuccess(
                SuccessStatus.PROBLEM_RECENT_HISTORY_SUCCESS,
                problemService.getRecentHistories(userId)
        );
    }
}
