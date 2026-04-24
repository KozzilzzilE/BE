package com.pocketco.domain.admin.dto;

public record AddLanguageSettingRequest(
        Long languageId,
        Integer timeLimitMs,
        String code
) {}