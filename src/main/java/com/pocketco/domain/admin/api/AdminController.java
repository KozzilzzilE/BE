package com.pocketco.domain.admin.api;

import com.pocketco.domain.admin.application.AdminService;
import com.pocketco.domain.admin.dto.AddLanguageRequest;
import com.pocketco.domain.admin.dto.AddLanguageResponse;
import com.pocketco.domain.admin.dto.AddTopicRequest;
import com.pocketco.domain.admin.dto.AddTopicResponse;
import com.pocketco.global.common.code.status.SuccessStatus;
import com.pocketco.global.common.response.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}