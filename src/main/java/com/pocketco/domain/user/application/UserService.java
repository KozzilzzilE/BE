package com.pocketco.domain.user.application;

import com.pocketco.domain.user.dto.MainScreenResponse;

public interface UserService {
    MainScreenResponse getMainScreenInfo(Long userId);
}