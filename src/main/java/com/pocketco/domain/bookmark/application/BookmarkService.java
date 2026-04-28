package com.pocketco.domain.bookmark.application;

import com.pocketco.domain.bookmark.dto.BookmarkResponseDTO.*;
import java.util.List;

public interface BookmarkService {
    // 설계도에 포함될 기능들 명시
    BookmarkToggleResponse addBookmark(Long userId, Long problemId);
    BookmarkToggleResponse removeBookmark(Long userId, Long problemId);
    List<BookmarkListResponse> getMyBookmarks(Long userId);
}