package com.pocketco.domain.language.exception;

import com.pocketco.global.common.code.status.ErrorStatus;
import com.pocketco.global.exception.GeneralException;

public class AlreadyExistsLanguageException extends GeneralException  {
    public AlreadyExistsLanguageException() {
        super(ErrorStatus.LANGUAGE_ALREADY_USED);
    }
}