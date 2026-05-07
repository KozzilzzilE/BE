package com.pocketco.domain.user.api;

import com.pocketco.domain.user.application.UserService;
import com.pocketco.domain.user.dto.MainScreenResponse;
import com.pocketco.global.common.code.status.SuccessStatus;
import com.pocketco.global.common.response.BaseResponse;
import com.pocketco.domain.user.dto.UserRequestDTO;
import com.pocketco.domain.user.dto.UserUpdateResponseDTO;
import com.pocketco.domain.user.dto.UserResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/main")
    public BaseResponse<MainScreenResponse> getMainScreen(@AuthenticationPrincipal Long userId) {
        MainScreenResponse result = userService.getMainScreenInfo(userId);
        return BaseResponse.onSuccess(SuccessStatus.USER_MAIN_SUCCESS, result);
    }
    // 1. 사용자 언어 변경
    @PatchMapping("/me/languages")
    public BaseResponse<UserUpdateResponseDTO> updateLanguage(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid UserRequestDTO.UpdateLanguageRequest request) {

        UserUpdateResponseDTO result = userService.updateLanguage(userId, request.language());
        return BaseResponse.onSuccess(SuccessStatus.LANGUAGE_UPDATE_SUCCESS, result);
    }

    // 2. 사용자 닉네임 변경
    @PatchMapping("/me/names")
    public BaseResponse<UserUpdateResponseDTO> updateNickname(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid UserRequestDTO.UpdateNicknameRequest request) {

        UserUpdateResponseDTO result = userService.updateNickname(userId, request.nickname());
        return BaseResponse.onSuccess(SuccessStatus.USER_UPDATE_SUCCESS, result);
    }
    @GetMapping("/me")
    public BaseResponse<UserResponseDTO.MyPageResponse> getMyPage(
            @AuthenticationPrincipal Long userId
    ) {
        return BaseResponse.onSuccess(
                SuccessStatus.USER_MYPAGE_SUCCESS,
                userService.getMyPage(userId)
        );
    }


}