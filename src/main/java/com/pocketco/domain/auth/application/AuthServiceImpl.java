package com.pocketco.domain.auth.application;

import com.pocketco.domain.auth.dto.LoginResponse;
import com.pocketco.domain.auth.dto.SignupRequest;
import com.pocketco.domain.auth.dto.SignupResponse;
import com.pocketco.domain.user.entity.Role;
import com.pocketco.domain.user.entity.User;
import com.pocketco.domain.user.entity.UserGoal;
import com.pocketco.domain.user.repository.UserRepository;
import com.pocketco.domain.user.repository.UserGoalRepository;
import com.pocketco.global.util.jwt.JwtTokenProvider; // 패키지 경로는 하은님 프로젝트에 맞춰 확인해주세요!
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserGoalRepository userGoalRepository;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 회원가입 로직: 유저 정보와 초기 목표를 함께 저장
     */
    public SignupResponse register(SignupRequest request) {
        String firebaseUid = verifyFirebaseToken(request.firebaseToken());

        User user = User.builder()
                .email(request.email())
                .nickname(request.nickname())
                .firebaseUid(firebaseUid) // 진짜 구글 신분증 번호 저장!
                .role(Role.USER)
                .build();

        User savedUser = userRepository.save(user);

        UserGoal goal = UserGoal.builder()
                .user(savedUser)
                .dailyTarget(0)
                .weeklyTarget(0)
                .monthlyTarget(0)
                .currentStreak(0)
                .totalSolved(0)
                .build();

        userGoalRepository.save(goal);

        return SignupResponse.builder()
                .userId(savedUser.getId())
                .email(savedUser.getEmail())
                .nickname(savedUser.getNickname())
                .build();
    }

    /**
     * 로그인 로직: DB에서 유저를 찾아 진짜 JWT 토큰을 발급
     */
    public LoginResponse login(String token) {
        String firebaseUid = verifyFirebaseToken(token);
        User user = userRepository.findByFirebaseUid(firebaseUid)
                .orElseThrow(() -> new RuntimeException("가입되지 않은 유저입니다."));

        String accessToken = jwtTokenProvider.createAccessToken(
                user.getId(),
                user.getEmail(),
                user.getRole().name()
        );

        return LoginResponse.builder()
                .accessToken(accessToken)
                .nickname(user.getNickname())
                .language("JAVA")
                .build();
    }
    private String verifyFirebaseToken(String token) {
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
            return decodedToken.getUid();
        } catch (Exception e) {
            throw new RuntimeException("유효하지 않은 파이어베이스 토큰입니다: " + e.getMessage());
        }
    }
}