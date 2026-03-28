package com.pocketco.global.exception.handler;

import com.pocketco.global.common.code.BaseErrorCode;
import com.pocketco.global.exception.GeneralException;

public class LanguageHandler extends GeneralException {
    public LanguageHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}