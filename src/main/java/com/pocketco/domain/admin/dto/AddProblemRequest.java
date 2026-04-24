package com.pocketco.domain.admin.dto;

import java.util.List;

public record AddProblemRequest(
        Long topicId,
        String title,
        String difficulty,
        Integer difficultyOrder,
        String description,
        String constraints,
        String lineSolution,
        String solutionText,
        List<AddTestCaseRequest> testCases,
        List<AddLanguageSettingRequest> languageSettings
) {}