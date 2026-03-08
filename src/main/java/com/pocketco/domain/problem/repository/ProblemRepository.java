package com.pocketco.domain.problem.repository;

import com.pocketco.domain.problem.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProblemRepository extends JpaRepository<Problem, Long> {
    boolean existsByTitle(String title);

    List<Problem> findAllByTopicIdOrderByDifficultyOrderAscIdAsc(Long topicId);
}
