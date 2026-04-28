package com.pocketco.domain.bookmark.dto;

import lombok.Builder;
import java.util.List;

public class BookmarkResponseDTO {

    @Builder
    public record BookmarkToggleResponse(boolean bookmarked) {}

    @Builder
    public record BookmarkListResponse(
            Long problemId,
            String title,
            String difficulty,
            String difficultyDisplayName,
            Long bookmarkCount
    ) {}
}