package com.pocketco.domain.language.exception;

import com.pocketco.global.common.code.status.ErrorStatus;
import com.pocketco.global.exception.GeneralException;

public class LanguageNotExists extends GeneralException {
    public LanguageNotExists() { super(ErrorStatus.LANGUAGE_NOT_EXISTS); }
}
