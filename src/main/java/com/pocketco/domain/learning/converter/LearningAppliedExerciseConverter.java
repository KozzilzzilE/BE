package com.pocketco.domain.learning.converter;

import com.pocketco.domain.learning.dto.LearningAppliedBlank;
import com.pocketco.domain.learning.dto.LearningAppliedExercise;
import com.pocketco.domain.learning.entity.applied.AppliedBlankProblem;
import com.pocketco.domain.learning.entity.applied.AppliedCode;
import com.pocketco.domain.learning.entity.applied.AppliedExercise;

import java.util.List;

public class LearningAppliedExerciseConverter {
    public static LearningAppliedExercise toExerciseResponse(
            AppliedExercise appliedExercise, AppliedCode appliedCode,
            boolean completed, List<AppliedBlankProblem> appliedBlanks) {
        LearningAppliedExercise.LearningAppliedExerciseBuilder builder =
                LearningAppliedExercise.builder()
                        .exerciseId(appliedExercise.getId())
                        .title(appliedExercise.getTitle())
                        .description(appliedExercise.getDescription())
                        .appliedCompleted(completed);

        // 아직 해당 언어로 빈칸 문제가 준비되지 않았을 경우 -> 빈칸들도 null
        if (appliedCode == null) {
            return builder
                    .codeTemplate("해당 언어로는 아직 빈칸 코드가 준비되지 않았습니다")
                    .totalBlanks(0)
                    .blanks(null)
                    .build();
        }

        // 해당 언어로 빈칸 코드는 준비됐지만 빈칸 문제는 준비 안되어 있을 때
        if (appliedBlanks == null) {
            return builder
                    .codeTemplate(appliedCode.getCodeTemplate() + "\n\n아직 빈칸은 준비되지 않았습니다")
                    .totalBlanks(0)
                    .blanks(null)
                    .build();
        }

        int count = (int) appliedBlanks.stream()
                .filter(blank -> blank.getAnswer() != null)
                .count();

        List<LearningAppliedBlank> blanks = appliedBlanks.stream()
                .map(blank -> LearningAppliedBlank.builder()
                        .answer(blank.getAnswer())
                        .content(blank.getContent())
                        .build())
                .toList();

        return builder
                .codeTemplate(appliedCode.getCodeTemplate())
                .totalBlanks(count)
                .blanks(blanks)
                .build();
    }
}