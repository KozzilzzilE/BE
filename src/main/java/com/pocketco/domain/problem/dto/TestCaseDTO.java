package com.pocketco.domain.problem.dto;

import lombok.Builder;

@Builder
public record TestCaseDTO(
        String input,
        String output
) {}