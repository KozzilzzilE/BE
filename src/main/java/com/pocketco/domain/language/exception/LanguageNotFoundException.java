package com.pocketco.domain.language.exception;

import com.pocketco.global.common.code.status.ErrorStatus;
import com.pocketco.global.exception.GeneralException;

public class LanguageNotFoundException extends GeneralException {
    public LanguageNotFoundException() {
        super(ErrorStatus.LANGUAGE_NOT_FOUND);
    }
}
