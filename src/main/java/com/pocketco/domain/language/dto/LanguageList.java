package com.pocketco.domain.language.dto;

import lombok.Builder;

@Builder
public record LanguageList(
        Long languageId,
        String name
) { }