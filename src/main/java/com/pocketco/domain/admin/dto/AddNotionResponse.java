package com.pocketco.domain.admin.dto;

import lombok.Builder;

@Builder
public record AddNotionResponse (
        Long notionId,
        Integer pageNo,
        String title,
        String imgUrl,
        Integer codeCount
) {}