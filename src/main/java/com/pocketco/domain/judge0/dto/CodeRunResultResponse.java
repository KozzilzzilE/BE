package com.pocketco.domain.judge0.dto;

import lombok.Builder;

@Builder
public record CodeRunResultResponse(
        int statusId,
        String status,
        String input,
        String output
) { }