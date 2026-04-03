package com.pocketco.domain.problem.repository;

import java.util.List;
import com.pocketco.domain.problem.entity.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestCaseRepository extends JpaRepository<TestCase, Long> {
    List<TestCase> findByProblemId(Long problemId);

    List<TestCase> findTop2ByProblemIdOrderByIdAsc(Long problemId);
}