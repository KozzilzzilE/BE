package com.pocketco.domain.user.api;

import com.pocketco.domain.user.application.UserService;
import com.pocketco.domain.user.dto.MainScreenResponse;
import com.pocketco.global.common.code.status.SuccessStatus;
import com.pocketco.global.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    // 2. UserServiceImpl 대신 UserService(인터페이스)로 바꿔주세요!
    private final UserService userService;

    @GetMapping("/main")
    public BaseResponse<MainScreenResponse> getMainScreen(@AuthenticationPrincipal Long userId) {
        MainScreenResponse result = userService.getMainScreenInfo(userId);
        return BaseResponse.onSuccess(SuccessStatus.USER_MAIN_SUCCESS, result);
    }
}