package com.pocketco.domain.admin.dto;

import lombok.Builder;

@Builder
public record AddPublicProfileImageResponse (
        Long profileId,
        String imgUrl
) { }