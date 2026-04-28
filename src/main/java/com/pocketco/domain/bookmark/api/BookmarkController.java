package com.pocketco.domain.bookmark.api;

import com.pocketco.domain.bookmark.application.BookmarkService;
import com.pocketco.domain.bookmark.dto.BookmarkResponseDTO.*;
import com.pocketco.global.common.code.status.SuccessStatus;
import com.pocketco.global.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookmarks/problems")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @PostMapping("/{problemId}")
    public BaseResponse<BookmarkToggleResponse> addBookmark(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long problemId) {
        return BaseResponse.onSuccess(SuccessStatus.BOOKMARK_ADD_SUCCESS, bookmarkService.addBookmark(userId, problemId));
    }

    @DeleteMapping("/{problemId}")
    public BaseResponse<BookmarkToggleResponse> removeBookmark(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long problemId) {
        return BaseResponse.onSuccess(SuccessStatus.BOOKMARK_DELETE_SUCCESS, bookmarkService.removeBookmark(userId, problemId));
    }

    @GetMapping("")
    public BaseResponse<List<BookmarkListResponse>> getBookmarks(@AuthenticationPrincipal Long userId) {
        return BaseResponse.onSuccess(SuccessStatus.BOOKMARK_LIST_SUCCESS, bookmarkService.getMyBookmarks(userId));
    }
}