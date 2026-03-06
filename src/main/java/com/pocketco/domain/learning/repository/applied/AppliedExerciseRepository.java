package com.pocketco.domain.learning.repository.applied;

import com.pocketco.domain.learning.entity.applied.AppliedExercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppliedExerciseRepository extends JpaRepository<AppliedExercise, Long> {
    boolean existsByTopic_IdAndOrderNo(Long topicId, Integer orderNo);
    List<AppliedExercise> findByTopic_IdOrderByOrderNoAsc(Long topicId);
}