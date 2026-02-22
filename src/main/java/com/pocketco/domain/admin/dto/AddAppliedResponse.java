package com.pocketco.domain.admin.dto;

import lombok.Builder;

@Builder
public record AddAppliedResponse(
        Long exerciseId,
        Integer orderNo,
        String title
) { }