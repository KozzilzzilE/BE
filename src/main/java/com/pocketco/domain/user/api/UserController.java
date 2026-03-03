package com.pocketco.domain.user.api;

import com.pocketco.domain.user.application.UserService;
import com.pocketco.domain.user.dto.UserResponseDTO;
import com.pocketco.global.common.code.status.SuccessStatus;
import com.pocketco.global.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/main")
    public BaseResponse<UserResponseDTO.MainScreenResponse> getMainScreen(
            @AuthenticationPrincipal Long userId) { // 🛡️ 보안 필터가 인증한 진짜 유저 ID

        UserResponseDTO.MainScreenResponse result = userService.getMainScreenInfo(userId);
        return BaseResponse.onSuccess(SuccessStatus.USER_PROFILE_SUCCESS, result);
    }
}