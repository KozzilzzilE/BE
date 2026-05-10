package com.pocketco.domain.admin.api;

import com.pocketco.domain.admin.application.AdminService;
import com.pocketco.domain.admin.dto.*;
import com.pocketco.domain.judge0.dto.Judge0LanguageResponse;
import com.pocketco.global.common.code.status.SuccessStatus;
import com.pocketco.global.common.response.BaseResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admins")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/languages/additions")
    public BaseResponse<AddLanguageResponse> addLanguage(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid AddLanguageRequest request) {
        AddLanguageResponse result = adminService.addLanguage(userId, request);
        return BaseResponse.onSuccess(SuccessStatus.ADMIN_ADD_LANGUAGE_SUCCESS, result);
    }

    @PostMapping("/topics/additions")
    public BaseResponse<AddTopicResponse> addTopic(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid AddTopicRequest request) {
        AddTopicResponse result = adminService.addTopic(userId, request);
        return BaseResponse.onSuccess(SuccessStatus.ADMIN_ADD_TOPIC_SUCCESS, result);
    }

    @PostMapping(value = "/notions/additions", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse<AddNotionResponse> addNotion(
            @AuthenticationPrincipal Long userId,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @Parameter(
                    description = "개념 페이지 요청 JSON",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AddNotionRequest.class)
                    )
            )
            @RequestPart("request") @Valid AddNotionRequest request) throws IOException {
        AddNotionResponse result = adminService.addNotion(userId, image, request);
        return BaseResponse.onSuccess(SuccessStatus.ADMIN_ADD_NOTION_SUCCESS, result);
    }

    @PostMapping("/applications/additions")
    public BaseResponse<List<AddAppliedResponse>> addApplication(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid AddAppliedRequestList request) {
        List<AddAppliedResponse> results = adminService.addApplied(userId, request.requests());
        return BaseResponse.onSuccess(SuccessStatus.ADMIN_ADD_APPLIED_EXERCISE_SUCCESS, results);
    }

    @PostMapping("/problems/additions")
    public BaseResponse<List<AddProblemResponse>> addProblems(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid AddProblemRequests requests) {
        List<AddProblemResponse> results = adminService.addProblems(userId, requests);
        return BaseResponse.onSuccess(SuccessStatus.ADMIN_ADD_PROBLEM_SUCCESS, results);
    }

    @GetMapping("/judge0/languages")
    public BaseResponse<List<Judge0LanguageResponse>> getJudge0Languages(@AuthenticationPrincipal Long userId) {
        List<Judge0LanguageResponse> results = adminService.judge0Languages(userId);
        return BaseResponse.onSuccess(SuccessStatus.ADMIN_JUDGE0_LANGUAGE_LIST, results);
    }

    @PostMapping("/problems/{problemId}/language-settings/{languageId}/additions")
    public BaseResponse<AddProblemLanguageSettingResponse> addProblemLanguageSetting(
            @AuthenticationPrincipal Long userId,
            @PathVariable(name = "problemId") Long problemId,
            @PathVariable(name = "languageId") Long languageId,
            @RequestBody @Valid AddProblemLanguageSettingRequest requests) {
        AddProblemLanguageSettingResponse result = adminService.addProblemLanguageSetting(userId, problemId, languageId, requests);
        return BaseResponse.onSuccess(SuccessStatus.ADMIN_ADD_PROBLEM_LANGUAGE_SETTING_SUCCESS, result);
    }

    @PostMapping("/cs-problems/additions")
    public BaseResponse<Long> addCsProblem(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid AddCSProblemRequest request) {
        Long result = adminService.addCSProblem(userId, request);
        return BaseResponse.onSuccess(SuccessStatus.ADMIN_ADD_CS_PROBLEM_SUCCESS, result);
    }

    @PostMapping("/notions/{notionId}/notion-codes/{languageId}/additions")
    public BaseResponse<AdminResponseDTO.AddNotionCodeResponse> addNotionCode(
            @AuthenticationPrincipal Long userId,
            @PathVariable(name = "notionId") Long notionId,
            @PathVariable(name = "languageId") Long languageId,
            @RequestBody @Valid AdminRequestDTO.AddNotionCodeRequest request) {

        AdminResponseDTO.AddNotionCodeResponse result = adminService.addNotionCode(userId, notionId, languageId, request);

        return BaseResponse.onSuccess(SuccessStatus.ADMIN_NOTION_CODE_SUCCESS, result);
    }

    @PostMapping("/applications/{exerciseId}/applied-codes/{languageId}/additions")
    public BaseResponse<AddExerciseAppliedCodeResponse> addAppliedCode(
            @AuthenticationPrincipal Long userId,
            @PathVariable(name = "exerciseId") Long exerciseId,
            @PathVariable(name = "languageId") Long languageId,
            @RequestBody @Valid AddExerciseAppliedCodeRequest request) {

        AddExerciseAppliedCodeResponse result = adminService.addAppliedCode(userId, exerciseId, languageId, request);

        return BaseResponse.onSuccess(SuccessStatus.ADMIN_APPLIED_CODE_SUCCESS, result);
    }

    @PostMapping(value = "/public-profile/additions", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse<AddPublicProfileImageResponse> addNotion(
            @AuthenticationPrincipal Long userId,
            @RequestPart(value = "image") MultipartFile image) throws IOException {
        AddPublicProfileImageResponse result = adminService.addPublicProfileImage(userId, image);
        return BaseResponse.onSuccess(SuccessStatus.ADMIN_ADD_PUBLIC_PROFILE_IMAGE_SUCCESS, result);
    }
}