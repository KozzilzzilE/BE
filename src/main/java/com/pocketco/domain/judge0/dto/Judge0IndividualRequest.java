package com.pocketco.domain.judge0.dto;
import com.fasterxml.jackson.annotation.JsonProperty;

public record Judge0IndividualRequest(
        @JsonProperty("source_code") String sourceCode,
        @JsonProperty("language_id") Integer languageCode,
        @JsonProperty("cpu_time_limit") Double cpuTimeLimit,
        @JsonProperty("memory_limit") Integer memoryLimit,
        @JsonProperty("stdin") String stdin,
        @JsonProperty("expected_output") String expectedOutput
) {}