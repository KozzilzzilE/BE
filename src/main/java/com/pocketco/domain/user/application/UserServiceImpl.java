package com.pocketco.domain.user.application;

import com.pocketco.domain.user.dto.MainScreenResponse;
import com.pocketco.domain.user.dto.UserUpdateResponseDTO;
import com.pocketco.domain.user.entity.User;
import com.pocketco.domain.user.exception.UserNotFoundException; //
import com.pocketco.domain.user.repository.UserRepository; //
import com.pocketco.domain.language.entity.Language;
import com.pocketco.domain.language.repository.LanguageRepository;
import com.pocketco.global.common.code.status.ErrorStatus;
import com.pocketco.global.exception.handler.LanguageHandler;
import com.pocketco.global.exception.handler.UserHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final LanguageRepository languageRepository;

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

    @Override
    @Transactional
    public UserUpdateResponseDTO updateLanguage(Long userId, String languageName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        Language language = languageRepository.findByName(languageName)
                .orElseThrow(() -> new LanguageHandler(ErrorStatus.LANGUAGE_NOT_FOUND));

        user.updateLanguage(language);
        return toUpdateResponseDTO(user);
    }

    @Override
    @Transactional
    public UserUpdateResponseDTO updateNickname(Long userId, String nickname) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        user.updateNickname(nickname);
        return toUpdateResponseDTO(user);
    }

    private UserUpdateResponseDTO toUpdateResponseDTO(User user) {
        return UserUpdateResponseDTO.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .language(user.getLanguage().getName())
                .build();
    }
}
