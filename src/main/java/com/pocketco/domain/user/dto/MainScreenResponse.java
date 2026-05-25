package com.pocketco.domain.user.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record MainScreenResponse(
        String nickname,
        Long languageId,
        String languageName,
        String profileImgUrl,
        List<MainScreenCalenderDTO> totalSolvedDetails,
        Integer thisMonthSolvedCount
) { }