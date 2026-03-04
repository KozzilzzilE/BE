package com.pocketco.domain.user.application;

import com.pocketco.domain.user.dto.MainScreenResponse;
import com.pocketco.domain.user.entity.User; //
import com.pocketco.domain.user.exception.UserNotFoundException; //
import com.pocketco.domain.user.repository.UserRepository; //
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public MainScreenResponse getMainScreenInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());

        return MainScreenResponse.builder()
                .nickname(user.getNickname())
                .languageId(user.getLanguage().getId())
                .languageName(user.getLanguage().getName())
                .build();
    }
}