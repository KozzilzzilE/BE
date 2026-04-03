package com.pocketco.domain.judge0.dto;
import com.fasterxml.jackson.annotation.JsonProperty;

public record Judge0IndividualRequest(
        @JsonProperty("source_code") String sourceCode,
        @JsonProperty("language_id") Integer languageId,
        @JsonProperty("stdin") String stdin,
        @JsonProperty("expected_output") String expectedOutput
) {}