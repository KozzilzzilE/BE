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
    @Operation(summary = "전체 코딩 문제 목록 조회 API", description = "난이도 필터링 및 페이징이 포함된 문제 목록을 조회합니다.")
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
    @Operation(summary = "코드 작성 임시 저장 API", description = "작성 중인 코드를 임시 저장하거나 기존 저장본을 업데이트합니다.")
    public BaseResponse<TempStorageResponseDTO> saveTempCode(
            @PathVariable(name = "problemId") Long problemId,
            @RequestParam(name = "language") String language,
            @RequestBody ProblemRequestDTO.TempStorageRequest request,
            @AuthenticationPrincipal User user
    ) {
        TempStorageResponseDTO result = problemService.saveOrUpdateTempCode(user, problemId, language, request);

        return BaseResponse.onSuccess(SuccessStatus.PROBLEM_TEMP_SAVE_SUCCESS, result);
    }
    @GetMapping("/{problemId}/temp-storages")
    @Operation(summary = "임시 저장 코드 조회 API", description = "저장된 코드가 있으면 반환하고, 없으면 null을 반환합니다.")
    public BaseResponse<TempStorageGetDTO> getTempCode(
            @PathVariable(name = "problemId") Long problemId,
            @RequestParam(name = "language") String language,
            @AuthenticationPrincipal User user
    ) {
        TempStorageGetDTO result = problemService.getTempCode(user, problemId, language);

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
