package com.pocketco.domain.judge0.dto;

import lombok.Builder;

@Builder
public record Judge0LanguageExternal(
        int id,
        String name
) { }