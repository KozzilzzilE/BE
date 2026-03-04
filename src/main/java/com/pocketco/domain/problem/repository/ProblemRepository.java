package com.pocketco.domain.problem.repository;

import com.pocketco.domain.problem.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemRepository extends JpaRepository<Problem, Long> {
    boolean existsByTitle(String title);
}