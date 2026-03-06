package com.pocketco.domain.learning.exception;

import com.pocketco.global.common.code.status.ErrorStatus;
import com.pocketco.global.exception.GeneralException;

public class NotionNotExistsException extends GeneralException {
    public NotionNotExistsException() { super(ErrorStatus.LEARNING_NOTION_NOT_EXISTS); }
}
