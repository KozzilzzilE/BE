package com.pocketco.domain.learning.exception;

import com.pocketco.global.common.code.status.ErrorStatus;
import com.pocketco.global.exception.GeneralException;

public class AlreadyExistsNotionCodeException extends GeneralException {
    public AlreadyExistsNotionCodeException() { super(ErrorStatus.LEARNING_NOTION_CODE_ALREADY_EXISTS); }
}
