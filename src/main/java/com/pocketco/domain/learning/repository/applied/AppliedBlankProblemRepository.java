package com.pocketco.domain.learning.repository.applied;

import com.pocketco.domain.learning.entity.applied.AppliedBlankProblem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface AppliedBlankProblemRepository extends JpaRepository<AppliedBlankProblem, Long> {
    List<AppliedBlankProblem> findByExerciseCode_IdIn(Collection<Long> exerciseCodeIds);
}