package com.pocketco.domain.problem.repository;

import com.pocketco.domain.problem.entity.TimeLimit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface TimeLimitRepository extends JpaRepository<TimeLimit, Long> {
    Optional<TimeLimit> findByProblem_IdAndLanguage_Id(Long problemId, Long languageId);
    boolean existsByProblem_IdAndLanguage_Id(Long problemId, Long languageId);
}