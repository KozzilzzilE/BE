package com.pocketco.domain.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddExerciseAppliedCodeResponse {
    private Long exerciseId;
    private String languageName;
    private Long appliedCodeId;
    private int blankCount;
}