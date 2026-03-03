package com.pocketco.domain.admin.dto;

public record AddSolutionCodeRequest(
        Long languageId,
        String code
) {}