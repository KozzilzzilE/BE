package com.pocketco.domain.csProblem.dto;

import lombok.Builder;

@Builder
public record CSProblemRandomResponseDTO(
        Long csProblemId,
        String question,
        Boolean answer,
        String explanation
) { }