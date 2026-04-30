package com.pocketco.domain.problem.repository;

import com.pocketco.domain.problem.entity.Problem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {

    boolean existsByTitle(String title);

    List<Problem> findAllByTopicIdOrderByDifficultyOrderAscIdAsc(Long topicId);

    // 1. 모든 문제 페이징 조회 (토픽 정보를 한 번에 Join해서 성능 최적화!)
    @EntityGraph(attributePaths = {"topic"})
    Page<Problem> findAll(Pageable pageable);

    // 2. 난이도별 문제 페이징 조회 (새로 추가!)
    @EntityGraph(attributePaths = {"topic"})
    Page<Problem> findAllByDifficulty(String difficulty, Pageable pageable);
}