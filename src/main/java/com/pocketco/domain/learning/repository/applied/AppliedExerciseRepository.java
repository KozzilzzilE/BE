package com.pocketco.domain.learning.repository.applied;

import com.pocketco.domain.learning.entity.applied.AppliedExercise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppliedExerciseRepository extends JpaRepository<AppliedExercise, Long> {
    boolean existsByTopic_IdAndOrderNo(Long topicId, Integer orderNo);
}