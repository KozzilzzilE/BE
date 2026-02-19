package com.pocketco.domain.admin.dto;

import lombok.Builder;

@Builder
public record AddLanguageResponse(
        Long languagesId
) {}