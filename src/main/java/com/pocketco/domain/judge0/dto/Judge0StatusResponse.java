package com.pocketco.domain.judge0.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Judge0StatusResponse(
        @JsonProperty("status")
        StatusDetail status
) {
    public record StatusDetail(
            int id,
            String description
    ) {}
}