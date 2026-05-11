package com.pocketco.domain.user.dto;

import lombok.Builder;

@Builder
public record UserUpdateResponseDTO(
        Long userId,
        String imgUrl,
        String email,
        String nickname,
        String language
) {}