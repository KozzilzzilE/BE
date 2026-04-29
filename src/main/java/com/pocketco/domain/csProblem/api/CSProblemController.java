package com.pocketco.domain.csProblem.api;

import com.pocketco.domain.csProblem.application.CSProblemService;
import com.pocketco.domain.csProblem.dto.CSProblemRandomResponseDTO;
import com.pocketco.global.common.code.status.SuccessStatus;
import com.pocketco.global.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cs-problems")
@RequiredArgsConstructor
public class CSProblemController {
    private final CSProblemService csProblemService;

    @GetMapping("/random")
    public BaseResponse<List<CSProblemRandomResponseDTO>> getRandomProblems(
            @RequestParam(name = "count", defaultValue = "5") int count) {
        List<CSProblemRandomResponseDTO> result = csProblemService.getRandomCSProblems(count);
        return BaseResponse.onSuccess(SuccessStatus.CS_PROBLEM_RANDOM_LIST_SUCCESS, result);
    }
}