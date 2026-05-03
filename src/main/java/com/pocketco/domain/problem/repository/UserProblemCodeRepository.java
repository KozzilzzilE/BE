package com.pocketco.domain.problem.repository;

import com.pocketco.domain.problem.entity.UserProblemCode;
import com.pocketco.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserProblemCodeRepository extends JpaRepository<UserProblemCode, Long> {
    // 유저, 문제ID, 언어 조합으로 저장된 코드가 있는지 확인하는 메서드
    Optional<UserProblemCode> findByUserAndProblemIdAndLanguage(User user, Long problemId, String language);
}