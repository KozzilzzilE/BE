package com.pocketco.domain.learning.dto;

import lombok.Builder;

@Builder
public record LearningNotion(
        Long notionId,
        Integer pageNo,
        String title,
        String point,
        String detail,
        String imgUrl,
        LearningNotionCode exampleCode,
        boolean notionCompleted
) { }