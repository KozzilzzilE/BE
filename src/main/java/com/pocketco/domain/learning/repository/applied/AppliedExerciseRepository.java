package com.pocketco.domain.learning.repository.applied;

import com.pocketco.domain.learning.entity.applied.AppliedExercise;
import org.springframework.data.repository.CrudRepository;

public interface AppliedExerciseRepository extends CrudRepository<AppliedExercise, Long> {
    boolean existsByTopic_IdAndOrderNo(Long topicId, Integer orderNo);
}