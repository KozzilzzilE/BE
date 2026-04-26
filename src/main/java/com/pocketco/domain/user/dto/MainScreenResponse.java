package com.pocketco.domain.user.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record MainScreenResponse(
        String nickname,
        Long languageId,
        String languageName,
        List<MainScreenCalenderDTO> totalSolvedDetails,
        Long thisMonthSolvedCount
) { }