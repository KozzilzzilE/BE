package com.pocketco.domain.user.application;

import com.pocketco.domain.user.dto.MainScreenResponse;
import com.pocketco.domain.user.dto.UserUpdateResponseDTO;
import com.pocketco.domain.user.dto.UserResponseDTO;

public interface UserService {
    MainScreenResponse getMainScreenInfo(Long userId);
    UserResponseDTO.MyPageResponse getMyPage(Long userId);

    UserUpdateResponseDTO updateLanguage(Long userId, String language);
    UserUpdateResponseDTO updateNickname(Long userId, String nickname);
}