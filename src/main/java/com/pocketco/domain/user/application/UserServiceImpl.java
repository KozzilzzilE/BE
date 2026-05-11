package com.pocketco.domain.user.application;

import com.pocketco.domain.user.dto.MainScreenCalenderDTO;
import com.pocketco.domain.user.dto.MainScreenResponse;
import com.pocketco.domain.user.dto.UserUpdateResponseDTO;
import com.pocketco.domain.user.entity.HistoryStatus;
import com.pocketco.domain.user.entity.PublicProfileImage;
import com.pocketco.domain.user.entity.User;
import com.pocketco.domain.user.exception.PublicProfileImageNotFoundException;
import com.pocketco.domain.user.exception.UserNotFoundException;
import com.pocketco.domain.user.repository.PublicProfileImageRepository;
import com.pocketco.domain.user.repository.UserRepository;
import com.pocketco.domain.user.dto.UserResponseDTO;
import com.pocketco.domain.language.entity.Language;
import com.pocketco.domain.language.repository.LanguageRepository;
import com.pocketco.global.util.file.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.pocketco.domain.user.repository.HistoryRepository;
import com.pocketco.domain.language.exception.LanguageNotFoundException;

import java.time.Instant;
import java.time.ZoneId;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final LanguageRepository languageRepository;
    private final HistoryRepository historyRepository;
    private final PublicProfileImageRepository publicProfileImageRepository;
    private final FileStorageService fileStorageService;

    @Override
    public MainScreenResponse getMainScreenInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        List<MainScreenCalenderDTO> dates = historyRepository.countSolvedByDate(userId).stream()
                        .map(p -> new MainScreenCalenderDTO(p.getDate(), p.getCount()))
                        .toList();

        ZoneId kst = ZoneId.of("Asia/Seoul");
        YearMonth thisMonth = YearMonth.now(kst);
        Instant startUtc = thisMonth
                .atDay(1)
                .atStartOfDay(kst)
                .toInstant();
        Instant endUtc = thisMonth
                .plusMonths(1)
                .atDay(1)
                .atStartOfDay(kst)
                .toInstant();
        int monthSoledCount = historyRepository.countThisMonthSolved(userId, HistoryStatus.ACCEPTED, startUtc, endUtc);

        return MainScreenResponse.builder()
                .nickname(user.getNickname())
                .languageId(user.getLanguage().getId())
                .languageName(user.getLanguage().getName())
                .totalSolvedDetails(dates)
                .thisMonthSolvedCount(monthSoledCount)
                .build();
    }

    @Override
    @Transactional
    public UserUpdateResponseDTO updateLanguage(Long userId, String languageName) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        Language language = languageRepository.findByName(languageName)
                .orElseThrow(LanguageNotFoundException::new);

        user.updateLanguage(language);
        return toUpdateResponseDTO(user);
    }

    @Override
    @Transactional
    public UserUpdateResponseDTO updateProfile(Long userId, String nickname, Long profileId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        PublicProfileImage profileImage = null;
        if (profileId != null) {
            profileImage = publicProfileImageRepository.findById(profileId).orElseThrow(PublicProfileImageNotFoundException::new);
        }

        user.updateProfile(nickname, profileImage);
        return toUpdateResponseDTO(user);
    }

    private UserUpdateResponseDTO toUpdateResponseDTO(User user) {
        String profileImageUrl = null;
        PublicProfileImage profileImage = user.getProfileImage();
        if (profileImage != null) {
            profileImageUrl = fileStorageService.getUrl(profileImage.getImgUrl());
        }

        return UserUpdateResponseDTO.builder()
                .userId(user.getId())
                .imgUrl(profileImageUrl)
                .email(user.getEmail())
                .nickname(user.getNickname())
                .language(user.getLanguage().getName())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO.MyPageResponse getMyPage(Long userId) {
        // 1. 사용자 정보 조회
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        // 2. 고유하게 해결한 문제 수 조회
        long solvedCount = historyRepository.countSolvedProblemUnique(userId);

        // 3. DTO 조립
        return UserResponseDTO.MyPageResponse.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .language(user.getLanguage() != null ? user.getLanguage().getName() : "JAVA")
                .solvedProblemCount(solvedCount)
                .build();
    }

}
