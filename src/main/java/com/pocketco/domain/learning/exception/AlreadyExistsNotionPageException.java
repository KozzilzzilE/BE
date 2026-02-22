package com.pocketco.domain.learning.exception;

import com.pocketco.global.common.code.status.ErrorStatus;
import com.pocketco.global.exception.GeneralException;

public class AlreadyExistsNotionPageException extends GeneralException {
    public AlreadyExistsNotionPageException() { super(ErrorStatus.LEARNING_NOTION_ALREADY_EXISTS); }
}