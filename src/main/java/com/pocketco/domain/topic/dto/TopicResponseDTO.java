package com.pocketco.domain.topic.dto;

import lombok.Builder;

@Builder
public record TopicResponseDTO(
        Long topicId,
        String name,
        String displayName
) {}