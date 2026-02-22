package com.pocketco.domain.admin.api;

import com.pocketco.domain.admin.application.AdminService;
import com.pocketco.domain.admin.dto.*;
import com.pocketco.global.common.code.status.SuccessStatus;
import com.pocketco.global.common.response.BaseResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
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
        return BaseResponse.onSuccess(SuccessStatus.ADMIN_ADD_LANGUAGE_SUCCESS, result);
    }

    @PostMapping(value = "/notions/additions", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse<AddNotionResponse> addNotion(
            @AuthenticationPrincipal Long userId,
            @RequestPart(value = "image", required = false) MultipartFile image,
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
}