package com.pocketco.domain.problem.repository;

import com.pocketco.domain.problem.entity.TimeLimit;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TimeLimitRepository extends JpaRepository<TimeLimit, Long> {
    TimeLimit findByProblem_IdAndLanguage_Id(Long problemId, Long languageId);
}