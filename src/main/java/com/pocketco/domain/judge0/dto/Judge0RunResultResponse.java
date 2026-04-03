package com.pocketco.domain.judge0.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Judge0RunResultResponse(
        String stdin,
        String stdout,
        @JsonProperty("status")
        StatusDetail status
) { }