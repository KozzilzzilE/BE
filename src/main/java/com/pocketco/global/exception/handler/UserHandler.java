package com.pocketco.global.exception.handler;

import com.pocketco.global.common.code.BaseErrorCode;
import com.pocketco.global.exception.GeneralException;

public class UserHandler extends GeneralException {
    public UserHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}