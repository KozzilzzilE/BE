package com.pocketco.domain.learning.repository.applied;

import com.pocketco.domain.language.entity.Language;
import com.pocketco.domain.learning.entity.applied.AppliedCode;
import com.pocketco.domain.learning.entity.applied.AppliedExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Collection;
import java.util.List;

public interface AppliedExerciseCodeRepository extends JpaRepository<AppliedCode, Long> {
    List<AppliedCode> findByExercise_IdInAndLanguage_Id(Collection<Long> exerciseIds, Long languageId);

    boolean existsByExerciseAndLanguage(AppliedExercise exercise, Language language);
}