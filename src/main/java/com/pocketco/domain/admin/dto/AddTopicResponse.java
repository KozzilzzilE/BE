package com.pocketco.domain.admin.dto;

import lombok.Builder;

@Builder
public record AddTopicResponse (
        Long topicId
) {}