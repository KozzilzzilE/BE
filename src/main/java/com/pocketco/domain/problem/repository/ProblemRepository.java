package com.pocketco.domain.problem.repository;

import com.pocketco.domain.problem.entity.Problem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProblemRepository extends JpaRepository<Problem, Long> {
    boolean existsByTitle(String title);

    List<Problem> findAllByTopicIdOrderByDifficultyOrderAscIdAsc(Long topicId);

    // ✅ 전체 목록 조회를 위한 메서드 (토픽 정보를 한 번에 Join해서 가져와서 성능 최적화!)
    @EntityGraph(attributePaths = {"topic"})
    Page<Problem> findAll(Pageable pageable);
}