package com.pocketco.domain.problem.repository;

import com.pocketco.domain.problem.entity.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestCaseRepository extends JpaRepository<TestCase, Long> {
}