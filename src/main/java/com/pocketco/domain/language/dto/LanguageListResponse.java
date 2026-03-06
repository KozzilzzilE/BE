package com.pocketco.domain.language.dto;

import lombok.Builder;

import java.util.List;
@Builder
public record LanguageListResponse(
        int count,
        List<LanguageList> languages
) { }