package com.pocketco.domain.problem.repository;

import com.pocketco.domain.problem.entity.SolutionCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolutionCodeRepository extends JpaRepository<SolutionCode, Long> {
}