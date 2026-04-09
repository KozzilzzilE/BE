package com.pocketco.domain.user.application;

import com.pocketco.domain.user.dto.MainScreenResponse;
import com.pocketco.domain.user.dto.UserUpdateResponseDTO;

public interface UserService {
    MainScreenResponse getMainScreenInfo(Long userId);

    UserUpdateResponseDTO updateLanguage(Long userId, String language);
    UserUpdateResponseDTO updateNickname(Long userId, String nickname);
}