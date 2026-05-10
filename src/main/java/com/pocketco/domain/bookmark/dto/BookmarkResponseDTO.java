package com.pocketco.domain.bookmark.dto;

import lombok.Builder;

public class BookmarkResponseDTO {

    @Builder
    public record BookmarkToggleResponse(boolean bookmarked) {}

    @Builder
    public record BookmarkListResponse(
            Long problemId,
            String title,
            String difficulty,
            String difficultyDisplayName,
            Long bookmarkCount,
            boolean isCompleted
    ) {}
}