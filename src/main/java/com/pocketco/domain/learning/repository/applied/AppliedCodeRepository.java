package com.pocketco.domain.learning.repository.applied;

import com.pocketco.domain.learning.entity.applied.AppliedCode;
import org.springframework.data.jpa.repository.JpaRepository;
import com.pocketco.domain.learning.entity.applied.AppliedExercise;
import com.pocketco.domain.language.entity.Language;
import java.util.Collection;
import java.util.List;

public interface AppliedCodeRepository extends JpaRepository<AppliedCode, Long> {
    List<AppliedCode> findByExercise_IdInAndLanguage_Id(Collection<Long> exerciseIds, Long languageId);

    boolean existsByExerciseAndLanguage(AppliedExercise exercise, Language language);
}
