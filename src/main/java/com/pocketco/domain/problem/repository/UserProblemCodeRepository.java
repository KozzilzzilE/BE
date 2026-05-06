package com.pocketco.domain.problem.repository;

import com.pocketco.domain.problem.entity.UserProblemCode;
import com.pocketco.domain.user.entity.User;
import com.pocketco.domain.language.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserProblemCodeRepository extends JpaRepository<UserProblemCode, Long> {
    Optional<UserProblemCode> findByUserAndProblemIdAndLanguage(User user, Long problemId, Language language);

    void deleteByUserAndProblemId(User user, Long problemId);
}