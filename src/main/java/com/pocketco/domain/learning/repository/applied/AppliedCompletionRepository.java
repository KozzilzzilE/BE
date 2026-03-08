package com.pocketco.domain.learning.repository.applied;

import com.pocketco.domain.learning.entity.applied.AppliedCompletion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface AppliedCompletionRepository extends JpaRepository<AppliedCompletion, Long> {
    List<AppliedCompletion> findByExercise_IdInAndUser_Id(Collection<Long> exerciseIds, Long userId);
}