package com.pocketco.domain.bookmark.exception;

import com.pocketco.global.common.code.status.ErrorStatus;
import com.pocketco.global.exception.GeneralException;

public class BookmarkAlreadyExistsException extends GeneralException {
    public BookmarkAlreadyExistsException() {
        super(ErrorStatus.BOOKMARK_ALREADY_EXISTS);
    }
}