package com.pocketco.domain.user.repository;

import com.pocketco.domain.user.entity.User;
import com.pocketco.domain.user.entity.UserCode;
import com.pocketco.domain.language.entity.Language;
import com.pocketco.domain.problem.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserCodeRepository extends JpaRepository<UserCode, Long> {
    Optional<UserCode> findByUserAndProblemAndLanguage(User user, Problem problem, Language language);

    void deleteByUserAndProblem(User user, Problem problem);
}