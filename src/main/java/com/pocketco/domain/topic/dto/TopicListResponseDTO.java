package com.pocketco.domain.topic.dto;

import lombok.Builder;
import java.util.List;

@Builder
public record TopicListResponseDTO(
        Integer count,
        List<TopicResponseDTO> topics
) {}