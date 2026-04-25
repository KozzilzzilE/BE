package com.pocketco.domain.judge0.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Judge0StatusResponse(
        @JsonProperty("status")
        StatusDetail status,

        @JsonProperty("time")
        Double time,

        @JsonProperty("wall_time")
        Double wallTime
) { }