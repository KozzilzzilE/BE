package com.pocketco.domain.bookmark.exception;

import com.pocketco.global.common.code.status.ErrorStatus;
import com.pocketco.global.exception.GeneralException;

public class BookmarkNotFoundException extends GeneralException {
    public BookmarkNotFoundException() {
        super(ErrorStatus.BOOKMARK_NOT_FOUND);
    }
}