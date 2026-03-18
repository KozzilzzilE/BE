package com.pocketco.domain.judge0.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record Judge0LanguageResponse(
        int code,
        String languageName
) { }