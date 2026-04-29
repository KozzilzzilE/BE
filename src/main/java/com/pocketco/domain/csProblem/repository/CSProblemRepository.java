package com.pocketco.domain.csProblem.repository;

import com.pocketco.domain.csProblem.entity.CSProblem;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CSProblemRepository extends JpaRepository<CSProblem, Long> {
    @Query(value = """
    select * 
    from cs_problems
    order by rand() limit :count
    """, nativeQuery = true)
    List<CSProblem> findRandomCSProblems(@Param("count") int count);
}